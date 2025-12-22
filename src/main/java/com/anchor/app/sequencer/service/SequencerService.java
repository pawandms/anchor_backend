package com.anchor.app.sequencer.service;

import com.anchor.app.sequencer.model.Sequance;

public interface SequencerService {
	
	
	/**
	 * Save Sequeance
	 * @param seq
	 * @return
	 */
	public Sequance saveSequence(Sequance seq);
	
	/**
	 * Get Sequence by Name
	 * @param name
	 * @return
	 */
	public Sequance getSequencebyName(String name);

}
