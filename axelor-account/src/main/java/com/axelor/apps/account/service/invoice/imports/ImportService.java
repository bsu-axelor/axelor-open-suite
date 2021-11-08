package com.axelor.apps.account.service.invoice.imports;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;

import com.axelor.data.csv.CSVImporter;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.google.common.io.Files;

public class ImportService {
	private File workspace;

	public void run(File bindFile, File dataFile) throws IOException {
		CSVImporter importer = new CSVImporter(bindFile.toString(), dataFile.getAbsolutePath());
		importer.run();
	}

	protected String computeFinalWorkspaceName(File data) {
		return String.format("%s-%s", Files.getNameWithoutExtension(data.getName()),
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
	}

	public File createFinalWorkspace(MetaFile metaFile) throws IOException {

		File data = MetaFiles.getPath(metaFile).toFile();
		File finalWorkspace = new File(workspace, computeFinalWorkspaceName(data));
		finalWorkspace.mkdir();

		FileUtils.copyFile(data, new File(finalWorkspace, metaFile.getFileName()));

		return finalWorkspace;
	}

}