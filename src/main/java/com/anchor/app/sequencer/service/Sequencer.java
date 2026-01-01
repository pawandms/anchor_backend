package com.anchor.app.sequencer.service;

import java.util.HashMap;

import com.anchor.app.config.SpringContext;
import com.anchor.app.sequencer.exceptions.SequencerException;
import com.anchor.app.sequencer.model.Sequance;



/**
 * A tool for the automatic generation of unique numbers. This class
 * goes to the database once every <code>MAX_KEYS</code> requests to
 * get a new seed for the numbers it generates. This class is thread-safe,
 * meaning multiple threads can be safely requesting unique numbers from it.
 * It is also multi-process safe. In other words, multiple machines can
 * simultaneously be generating unique values and those values will
 * be guaranteed to be unique across all applications. The only caveat
 * is that they all must be using the same algorithm for generating
 * the numbers and getting seeds from the same database. In order to
 * access the database, this class expects a system property called
 * <code>org.dasein.persist.SequencerDSN</code>. It should be set to the
 * name of the DSN that provides connections to the database with the
 * <code>Sequencer</code> table. That table should have the
 * following <code>CREATE</code>:
 * <span class="code">
 * CREATE TABLE Sequencer (
 *     name        VARCHAR(20)     NOT NULL,
 *     seed        BIGINT UNSIGNED NOT NULL,
 *     lastUpdate  BIGINT UNSIGNED NOT NULL,
 *     PRIMARY KEY ( name, lastUpdate )
 * );
 * </span>
 * <br/>
 * Last modified $Date$
 * @version $Revision$
 * 
 */

public class Sequencer {
    
	private SequencerService getSequenceService()
	{
		return SpringContext.getBean(SequencerService.class);
	}
	
	/**
     * The maximum number of keys that may be safely generated without
     * going to the database. You should lower this number for client
     * applications and other short-lived programs. The number can be
     * higher for applications with long uptimes. All applications
     * using the same sequencer, however, should have the same value
     * for <code>MAX_KEYS</code>.
     */
	/* Modified by Pawan on 09-06-2019 for Performance Testing */
    //static private final long    MAX_KEYS   = 5L;
	static private final long    MAX_KEYS   = 500L;
    /**
     * All sequencers currently in memory.
     */
    static private final HashMap<String, Sequencer> sequencers = new HashMap<String, Sequencer>();

    /**
     * Looks to see if a sequencer has been generated for the sequence
     * with the specified name. If not, it will instantiate one.
     * Multiple calls to this method with the same name are guaranteed
     * to receive the same sequencer object. For best performance,
     * classes should save a reference to the sequencer once they get it
     * in order to avoid the overhead of a <code>HashMap</code> lookup.
     * @param name the name of the desired sequencer
     * @return the sequencer with the specified name
     */
    static public final Sequencer getInstance(String name) {
        synchronized( sequencers ) {
            if( !sequencers.containsKey(name) ) {
                Sequencer seq = new Sequencer(name);

                sequencers.put(name, seq);
                return seq;
            }
            else {
                return (Sequencer)sequencers.get(name);
            }
        }
    }

    /**
     * The name of this sequencer.
     */
    private String name     = null;
    /**
     * The seed this sequencer will use for generating its ID's.
     */
    private long   seed     = -1L;
    /**
     * The current sequence within this sequencer's seed.
     */
    private long   sequence = 0L;

    /**
     * Constructs a new sequencer with the specified name.
     * @param nom the name of the sequencer
     */
    private Sequencer(String nom) {
        super();
        name = nom;
    }
    
   /**
     * The SQL for creating a new sequence in the database.
     */
    static private final String CREATE_SEQ =
        "INSERT INTO gt_cr_sequencer ( name, seed, lastUpdate ) " +
        "VALUES ( ?, ?, ? )";
    /**
     * Constant for the name parameter.
     */
    static private final int INS_NAME   = 1;
    /**
     * Constant for the seed parameter.
     */
    static private final int INS_SEED   = 2; 
    /**
     * Constant for the lastUpdate parameter
     */
    static private final int INS_UPDATE = 3;
    
    /**
     * Creates a new entry in the database for this sequence. This method
     * will throw an error if two threads are simultaneously trying
     * to create a sequence. This state should never occur if you
     * go ahead and create the sequence in the database before
     * deploying the application. It could be avoided by checking
     * SQL exceptions for the proper XOPEN SQLState for duplicate
     * keys. Unfortunately, that approach is error prone due to the lack
     * of consistency in proper XOPEN SQLState reporting in JDBC drivers.
     * @param conn the JDBC connection to use
     * @throws java.sql.SQLException a database error occurred
     * @throws SequencerException 
     */
    private void create() throws SequencerException {
        
        try {
        	Sequance seq = new Sequance();
        	seq.setName(name);
        	seq.setSeed(0L);
        	seq.setLastupdate(System.currentTimeMillis());
        	
        	getSequenceService().saveSequence(seq);
            seed = 0L;
        }
        catch(Exception e)
        {
         throw new SequencerException(e.getMessage(), e);		
        }
        finally {
            
        }
    }

    /**
     * The name of a DSN to use if none is configured in the system
     * properties.
     */
    static private final String DEFAULT_DSN = "jdbc/kyra";
    /**
     * The name of the system property to check for a DSN.
     */
    static private final String DSN_PROP    = "org.dasein.persist.DSN";

    /**
     * Generates a new unique number. The unique number is based on the
     * following algorithm:<br/>
     * <i>unique number</i> = <i>seed</i> multiple by
     * <i>maximum keys per seed</i> added to <i>seed sequence</i>
     * <br/>
     * The method then increments the seed sequence for the next
     * ID to be generated. If the ID to be generated would exhaust
     * the seed, then a new seed is retrieved from the database.
     * @return a unique number
     * @throws com.gtd.core.sequencer.exceptions.dasein.persist.PersistenceException a data store error
     * occurred while generating the number
     */
    public synchronized long next() throws SequencerException {
       
        // when seed is -1 or the keys for this seed are exhausted,
        // get a new seed from the database
        if( (seed == -1L) || ((sequence + 1) >= MAX_KEYS) ) {
            try {
                reseed();
            }
            catch( Exception e ) {
                throw new SequencerException(e.getMessage(),e);
            }            
            
            finally {
                
            }
        }
        // up the sequence value for the next key
        sequence++;
        // the next key for this sequencer
        return (seed * MAX_KEYS) + sequence;
    }

    /**
     * The SQL for getting a seed for a sequence from the database.
     */
    static private final String FIND_SEQ =
        "SELECT seed, lastUpdate " +
        "FROM gt_cr_sequencer " +
        "WHERE name = ?";
    /**
     * Constant for the name parameter.
     */
    static private final int SEL_NAME   = 1;
    /**
     * Constant for the seed column.
     */
    static private final int SEL_SEED   = 1;
    /**
     * Constant for the lastUpdate column.
     */
    static private final int SEL_UPDATE = 2;
    /**
     * The SQL for incrementing the seed in the database.
     */
    static private String UPDATE_SEQ =
        "UPDATE gt_cr_sequencer " +
        "SET seed = ?, " +
        "lastUpdate = ? " +
        "WHERE name = ? AND lastUpdate = ?";
    /**
     * Constant for the seed parameter.
     */
    static private final int UPD_SEED         = 1;
    /**
     * Constant for the lastUpdate set parameter
     */
    static private final int UPD_SET_UPDATE   = 2;
    /**
     * Constant for the name parameter.
     */
    static private final int UPD_NAME         = 3;
    /**
     * Constant for the lastUpdate parameter.
     */
    static private final int UPD_WHERE_UPDATE = 4;

    /**
     * Gets the next seed from the database for this sequence.
     * @param conn the database connection
     * @throws java.sql.SQLException a database error occurred
     */
    private void reseed() throws SequencerException {
   
        try {
            // Keep in this loop as long as we encounter concurrency errors
            do {
            	
            	Sequance seq = getSequenceService().getSequencebyName(name);
            	
            	// No Such Sequence in DB hence Create it
            	if(null == seq)
            	{
            	create();	
            	}
            	else {
                    long ts;
                    seed = seq.getSeed() + 1L;
                    ts = seq.getLastupdate();
                    // increment the seed in the database
                    seq.setSeed(seed);
                    seq.setLastupdate(System.currentTimeMillis());
                    getSequenceService().saveSequence(seq);
                    
                   		
            	}

            } while( seed == -1L );
            sequence = -1L;
        }
        catch(Exception e)
        {
        	throw new SequencerException(e.getMessage(), e);
        }
        finally {
            
        }   
    }
}
