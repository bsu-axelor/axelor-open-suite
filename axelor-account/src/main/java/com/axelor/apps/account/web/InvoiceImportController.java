package com.axelor.apps.account.web;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.axelor.apps.account.db.FECImport;
import com.axelor.apps.account.db.repo.FECImportRepository;
import com.axelor.apps.account.service.invoice.imports.ImportService;
import com.axelor.apps.base.db.ImportConfiguration;
import com.axelor.apps.base.db.ImportHistory;
import com.axelor.apps.base.db.Wizard;
import com.axelor.apps.base.db.repo.WizardRepository;
import com.axelor.apps.base.service.imports.importer.FactoryImporter;
import com.axelor.apps.base.service.imports.importer.Importer;
import com.axelor.exception.service.TraceBackService;
import com.axelor.inject.Beans;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.repo.MetaFileRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceImportController {
	@Inject
	ImportService importService;

	public void run(ActionRequest request, ActionResponse response) {

		Wizard wizard = request.getContext().asType(Wizard.class);
		try {

			Map<String, Object> bindMap = (LinkedHashMap<String, Object>) request.getContext().get("bindMetaFile");

			Map<String, Object> dataMap = (LinkedHashMap<String, Object>) request.getContext().get("dataMetaFile");

			MetaFile bindFile = Beans.get(MetaFileRepository.class).find(Long.parseLong(bindMap.get("id").toString()));
			MetaFile dataFile = Beans.get(MetaFileRepository.class).find(Long.parseLong(dataMap.get("id").toString()));
			File fileWorkspace = importService.createFinalWorkspace(dataFile);
			
			File bindFile1 = MetaFiles.getPath( bindFile).toFile();
		
			importService.run(bindFile1, fileWorkspace);

		} catch (Exception e) {
			response.setError("Error while importing");
			TraceBackService.trace(response, e);
		}
	}
}
