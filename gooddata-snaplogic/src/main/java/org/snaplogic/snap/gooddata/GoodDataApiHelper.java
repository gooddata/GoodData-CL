package org.snaplogic.snap.gooddata;

import com.gooddata.exception.HttpMethodException;
import com.gooddata.integration.model.Project;
import com.gooddata.integration.rest.GdcRESTApiWrapper;

import java.util.HashMap;
import java.util.List;

public class GoodDataApiHelper {
    protected static String getProjectLabel(Project project) {
        return project.getName() + " - " + project.getId();
    }

    public static HashMap<String, Project> getProjectList(GdcRESTApiWrapper restApi) throws HttpMethodException {
        List<Project> projects = restApi.listProjects();

        HashMap<String, Project> projNames = new HashMap<String, Project>();
        for (Project proj : projects) {
            projNames.put(getProjectLabel(proj), proj);
        }
        return projNames;
    }
}