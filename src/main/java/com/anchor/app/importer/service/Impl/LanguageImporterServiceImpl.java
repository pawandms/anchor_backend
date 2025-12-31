package com.anchor.app.importer.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anchor.app.exception.ImporterException;
import com.anchor.app.importer.model.LanguageImport;
import com.anchor.app.importer.repository.LanguageImporterRepository;
import com.anchor.app.importer.service.LanguageImporterService;
import com.anchor.app.model.Language;
import com.anchor.app.repository.LanguageRepository;
import com.anchor.app.util.HelperBean;

@Service
public class LanguageImporterServiceImpl implements LanguageImporterService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HelperBean helper;

	@Autowired
	private LanguageImporterRepository lanImpRep;

	@Autowired
	private LanguageRepository lanRep;
	
	@Override
	public int importLanguageData() throws ImporterException 
	{
		int result = 0;
		
		List<LanguageImport> lanList = lanImpRep.findAll();
		
		List<Language> languageList =  importLanguageData(lanList);
		
		lanRep.saveAll(languageList);
		
		result = languageList.size();
		
		return result;
		
	}
	
	private List<Language> importLanguageData(List<LanguageImport> lanList)
	{
		List<Language> languageList = new ArrayList<Language>();
		
		lanList.stream().forEach(ilan -> {
				try {
				
					Language lan = helper.convertLanguageImporttoLanguage(ilan);
					languageList.add(lan);
					
				} catch (Exception e) {
					// Swallowing Parallel Stream exception
					logger.error("Import Error for :"+ilan.getName(), e);
				}
			});
	
		return languageList;
	}

}
