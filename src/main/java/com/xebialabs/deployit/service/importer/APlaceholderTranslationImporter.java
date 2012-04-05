package com.xebialabs.deployit.service.importer;

import com.xebialabs.deployit.server.api.importer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class APlaceholderTranslationImporter implements ListableImporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(APlaceholderTranslationImportSource.class);
	static final String DELEGATED_SERVICE = "com.xebialabs.deployit.server.api.importer.ManifestBasedDarImporter";

	private ListableImporter service;
	private APlaceholderTranslationImportSource translationImportSource;

	public APlaceholderTranslationImporter() {
	}

	public List<String> list(File directory) {
		return Collections.emptyList();
	}

	public boolean canHandle(ImportSource source) {
		//only dar file are supported.
		File file = source.getFile();
		return isDarPackage(file);
	}

	public PackageInfo preparePackage(ImportSource source, ImportingContext context) {
		LOGGER.debug("source = {}", source);
		translationImportSource = new APlaceholderTranslationImportSource(source);
		return getService().preparePackage(translationImportSource, context);
	}

	public ImportedPackage importEntities(PackageInfo packageInfo, ImportingContext context) {
		return getService().importEntities(packageInfo, context);
	}

	public void cleanUp(PackageInfo packageInfo, ImportingContext context) {
		getService().cleanUp(packageInfo, context);
		translationImportSource.cleanUp();
	}

	static boolean isDarPackage(final File f) {
		return f.isFile() && f.getName().toLowerCase().endsWith(".dar");
	}

	public ListableImporter getService() {
		if (service == null) {
			try {
				final Class<?> aClass = this.getClass().getClassLoader().loadClass(DELEGATED_SERVICE);
				service = (ListableImporter) aClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Cannot instance class " + DELEGATED_SERVICE, e);
			}
		}
		return service;
	}
}
