package org.nuxeo.sample;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.PathRef;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.platform.content.template.service.PostContentCreationHandler;

/**
 * This ContentCreationHandler adds a child document to all domains.
 */
public class CustomContentCreationHandler implements PostContentCreationHandler {
    public static final String DC_TITLE_PROPERTY_NAME = "dc:title";
    public static final String DC_DESCRIPTION_PROPERTY_NAME = "dc:description";
    public static final String GET_ALL_DOMAINS = "Select * From Domain";
    private static final String NEW_DOCUMENT_TYPE = "WorkspaceRoot";
    private static final String CHILD_DOC_NAME = "anotherworkspaceroot";
    public static final String DC_TITLE = "Another Workspace root";
    public static final String DC_DESCRIPTION = "The Description";
    private static final Log log = LogFactory.getLog(CustomContentCreationHandler.class);

    @Override
    public void execute(CoreSession session) {
        try {
            DocumentModelList domains = session.query(GET_ALL_DOMAINS);
            for (DocumentModel domain : domains) {
                PathRef ref = new PathRef(domain.getPathAsString(), CHILD_DOC_NAME);
                if (!session.exists(ref)) {
                    DocumentModel childDocToCreate = session.createDocumentModel(
                            domain.getPathAsString(), CHILD_DOC_NAME,
                            NEW_DOCUMENT_TYPE);
                    childDocToCreate.setPropertyValue(DC_TITLE_PROPERTY_NAME,
                            DC_TITLE);
                    childDocToCreate.setPropertyValue(
                            DC_DESCRIPTION_PROPERTY_NAME, DC_DESCRIPTION);                    
                    session.createDocument(childDocToCreate);
                }
            }
        } catch (Exception e) {
            throw new NuxeoException(e);
        }
    }
}
