package com.xebialabs.deployit.service.importer;

import com.xebialabs.deployit.cli.ext.mustachify.Mustachifier;
import com.xebialabs.deployit.server.api.importer.ImportSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class APlaceholderTranslationImportSource implements ImportSource {

	private static final Logger LOGGER = LoggerFactory.getLogger(APlaceholderTranslationImportSource.class);

	private final ImportSource original;
	private File mustachiedFile;

	public APlaceholderTranslationImportSource(ImportSource original) {
		this.original = original;
	}

	public File getFile() {
		if (mustachiedFile == null) {
			try {
				mustachiedFile = new Mustachifier(null).convert(original.getFile().getAbsolutePath());
				LOGGER.debug("mustachiedFile = " + mustachiedFile);
			} catch (IOException e) {
				throw new RuntimeException("Cannot Mustache the file " + original.getFile());
			}
		}
		return mustachiedFile;
	}

	public void cleanUp() {
		if (mustachiedFile != null) {
			LOGGER.debug("Cleanup " + mustachiedFile);
			mustachiedFile.delete();
		}
		original.cleanUp();
	}
}
