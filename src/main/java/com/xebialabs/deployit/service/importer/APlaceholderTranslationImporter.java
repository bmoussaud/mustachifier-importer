package com.xebialabs.deployit.service.importer;

import com.xebialabs.deployit.server.api.importer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static com.xebialabs.deployit.service.importer.PackageScanner.isDarPackage;

public class APlaceholderTranslationImporter implements ListableImporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(APlaceholderTranslationImportSource.class);

	private final ListableImporter service = new ManifestBasedDarImporter();
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
		return service.preparePackage(translationImportSource, context);
	}

	public ImportedPackage importEntities(PackageInfo packageInfo, ImportingContext context) {
		return service.importEntities(packageInfo, context);
	}

	public void cleanUp(PackageInfo packageInfo, ImportingContext context) {
		service.cleanUp(packageInfo, context);
		translationImportSource.cleanUp();
	}
}
