package com.bxy.salestrategies;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

public class FolderResource implements IResource {
	private static final Logger logger = Logger.getLogger(FolderResource.class);
	private final File rootFolder;

    public FolderResource(File rootFolder) {
    	this.rootFolder = rootFolder;
    }

	@Override
	public void respond(Attributes attributes) {
		PageParameters parameters = attributes.getParameters();
		logger.debug(parameters.getNamedKeys());
        String fileName = parameters.get(0).toString();
        File file = new File(rootFolder, fileName);
        try {
			logger.debug("file:"+file.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        FileResourceStream fileResourceStream = new FileResourceStream(file);
        ResourceStreamResource resource = new ResourceStreamResource(fileResourceStream);
        resource.respond(attributes);
		
	}
}