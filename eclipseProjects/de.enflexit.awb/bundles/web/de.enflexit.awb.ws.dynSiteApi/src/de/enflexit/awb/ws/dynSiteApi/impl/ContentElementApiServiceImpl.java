package de.enflexit.awb.ws.dynSiteApi.impl;

import java.io.IOException;
import de.enflexit.awb.ws.core.db.WebAppDatabaseHandler;
import de.enflexit.awb.ws.core.db.dataModel.SiteContent;
import de.enflexit.awb.ws.core.exceptions.DatabaseException;
import de.enflexit.awb.ws.dynSiteApi.content.TypeConverter;
import de.enflexit.awb.ws.dynSiteApi.gen.ApiResponseMessage;
import de.enflexit.awb.ws.dynSiteApi.gen.ContentElementApiService;
import de.enflexit.awb.ws.dynSiteApi.gen.NotFoundException;
import de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent;
import de.enflexit.awb.ws.dynSiteApi.gen.model.CreateContentElement201Response;
import de.enflexit.awb.ws.dynSiteApi.gen.model.CreateContentRequest;
import de.enflexit.awb.ws.dynSiteApi.gen.model.UpdateContentElementRequest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;

/**
 * The Class ContentElementApiServiceImpl.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2026-09-23T10:17:15.337896100+02:00[Europe/Berlin]", comments = "Generator version: 7.25.0")
public class ContentElementApiServiceImpl extends ContentElementApiService {
    
	private WebAppDatabaseHandler webAppDatabaseHandler;
	
    /* (non-Javadoc)
    * @see de.enflexit.awb.ws.dynSiteApi.gen.ContentElementApiService#createContentElement(de.enflexit.awb.ws.dynSiteApi.gen.model.CreateContentRequest, jakarta.ws.rs.core.SecurityContext)
    */
    @Override
    public Response createContentElement(CreateContentRequest createContentRequest, SecurityContext securityContext) throws NotFoundException {

    	if (createContentRequest.getMenuId() == null || createContentRequest.getPosition() == null || createContentRequest.getContent() == null) {
    		return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "Menu id, position or content is missing")).build();
    	}
    	// --- Convert the received content element into database SiteContent ---------------------
    	SiteContent contentElement = TypeConverter.toDBSiteContent(createContentRequest.getContent());
    	
    	// --- Save the SiteContent and return the result -----------------------------------------
    	try {
			this.getDatabaseHandler().persistNewContentElement(contentElement, createContentRequest.getMenuId(), createContentRequest.getPosition());
			
		} catch (IllegalArgumentException e) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, e.getMessage())).build();
			
		} catch (IOException | DatabaseException e) {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "The requested service is currently unavailable.")).build();
		}
    	
    	return Response.status(Status.CREATED).entity(new CreateContentElement201Response().elementId(contentElement.getId())).build();
    }
    
    
    /* (non-Javadoc)
    * @see de.enflexit.awb.ws.dynSiteApi.gen.ContentElementApiService#deleteContentElement(java.lang.Integer, jakarta.ws.rs.core.SecurityContext)
    */
    @Override
    public Response deleteContentElement(Integer elementID, SecurityContext securityContext) throws NotFoundException {
    	
    	if (elementID == null) {
    		return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "id is missing")).build();
    	}
    	
    	try {
			this.getDatabaseHandler().deleteContentElementById(elementID);
			
		} catch (IllegalArgumentException iae) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, iae.getMessage())).build();
			
		} catch (IOException | DatabaseException e) {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "The requested service is currently unavailable.")).build();
			
		} 
    	return Response.ok().build();
    }
    
    /* (non-Javadoc)
    * @see de.enflexit.awb.ws.dynSiteApi.gen.ContentElementApiService#getContentElement(java.lang.Integer, jakarta.ws.rs.core.SecurityContext)
    */
    @Override
    public Response getContentElement(Integer elementID, SecurityContext securityContext) throws NotFoundException {
    	
    	if (elementID == null) {
    		return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "id is missing")).build();
    	}
    	
    	try {
    		AbstractSiteContent requestedContent = TypeConverter.toRestContent(this.getDatabaseHandler().getContentElementById(elementID));

    		return Response.ok().entity(requestedContent).build();
    		
    	} catch (IllegalArgumentException iae) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, iae.getMessage())).build();
			
		} catch (IOException | DatabaseException e) {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "The requested service is currently unavailable.")).build();
			
		} 
    }
    
    /* (non-Javadoc)
    * @see de.enflexit.awb.ws.dynSiteApi.gen.ContentElementApiService#updateContentElement(java.lang.Integer, de.enflexit.awb.ws.dynSiteApi.gen.model.AbstractSiteContent, jakarta.ws.rs.core.SecurityContext)
    */
    @Override
    public Response updateContentElement(Integer elementID, UpdateContentElementRequest updateContentElementRequest, SecurityContext securityContext) throws NotFoundException {
    	
    	if (elementID == null || updateContentElementRequest == null) {
    		return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "id or site content is missing")).build();
    	}
    	
    	try {
    		this.getDatabaseHandler().updateContent(elementID, TypeConverter.toDBSiteContent(updateContentElementRequest.getContent()));
    		return Response.ok().build();
    		
    	} catch (IllegalArgumentException iae) {
			return Response.status(Status.BAD_REQUEST).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, iae.getMessage())).build();
			
		} catch (IOException | DatabaseException e) {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(new ApiResponseMessage(ApiResponseMessage.ERROR, "The requested service is currently unavailable.")).build();
			
		} 
    }
    
    /**
     * Returns the database handler.
     *
     * @return the database handler
     */
    private WebAppDatabaseHandler getDatabaseHandler() {
    	if (webAppDatabaseHandler == null) {
    		webAppDatabaseHandler = new WebAppDatabaseHandler();
    	}
    	return webAppDatabaseHandler;
    }
    
}