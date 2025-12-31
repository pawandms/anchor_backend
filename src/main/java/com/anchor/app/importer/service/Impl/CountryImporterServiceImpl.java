package com.anchor.app.importer.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.exception.ImporterException;
import com.anchor.app.importer.model.CountryImport;
import com.anchor.app.importer.repository.CountryImporterRepository;
import com.anchor.app.importer.service.CountryImporterService;
import com.anchor.app.model.Country;
import com.anchor.app.repository.CountryRepository;
import com.anchor.app.util.HelperBean;

@Service
public class CountryImporterServiceImpl implements CountryImporterService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HelperBean helper;

	@Autowired
	private CountryImporterRepository conImpRep;

	@Autowired
	private CountryRepository conRep;
	
	@Override
	public int importCountryData() throws ImporterException 
	{
		int result = 0;
		
		List<CountryImport> conImpList = conImpRep.findAll();
		
		List<Country> conList =  importCountryData(conImpList);
		
		conRep.saveAll(conList);
		
		result = conList.size();
		
		return result;
		
	}
	
	private List<Country> importCountryData(List<CountryImport> conImpList)
	{
		List<Country> conList = new ArrayList<Country>();
		
		conImpList.stream().forEach(icon -> {
				try {
				
					Country con = helper.convertCountryImporttoCountry(icon);
					conList.add(con);
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+icon.getName(), e);
				}
			});
	
		return conList;
	}

}
