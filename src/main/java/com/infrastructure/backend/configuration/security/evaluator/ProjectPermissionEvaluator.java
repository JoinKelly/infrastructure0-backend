package com.infrastructure.backend.configuration.security.evaluator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Component
public class ProjectPermissionEvaluator implements PermissionEvaluator {

    final static Logger logger = LoggerFactory.getLogger(ProjectPermissionEvaluator.class);

    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object o1) {
        try {

            List<String> permissions = this.extractPermissions(o1);
            return false;
        } catch (Exception e) {
            logger.error("Error in security expression: {}", e);
            return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        logger.error("wrong security validator!");
        return false;
    }

    public List<String> extractPermissions(Object permissions) {
        logger.debug("This is permission service1");
        String permissionsString[] = ((String) permissions).split(",");
        return Arrays.asList(permissionsString);
    }

}
