package com.infrastructure.backend.configuration.security.evaluator;

import com.infrastructure.backend.common.exception.CustomResponseStatusException;
import com.infrastructure.backend.common.exception.ErrorCode;
import com.infrastructure.backend.entity.project.Project;
import com.infrastructure.backend.entity.user.Role;
import com.infrastructure.backend.entity.user.User;
import com.infrastructure.backend.repository.ProjectRepository;
import com.infrastructure.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Component
public class ProjectPermissionEvaluator implements PermissionEvaluator {

    final static Logger logger = LoggerFactory.getLogger(ProjectPermissionEvaluator.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object o1) {
        try {

            User user = this.userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_EXIST.name(), "User is not exist"));
            if (user.getRoles() != null) {
                for (Role role: user.getRoles()) {
                    if (role.getName().equals("ADMINISTRATOR")
                            && (o1.equals("ADD_MEMBER")
                                || o1.equals("ADD_MEMBER")
                                || o1.equals("REMOVE_MEMBER")
                                || o1.equals("FIND_ALL_MEMBER"))) {
                        return true;
                    }
                }
            }

            Integer projectId = Integer.valueOf(o.toString());
            Project project = this.projectRepository.findById(projectId).orElseThrow(() -> new CustomResponseStatusException(HttpStatus.NOT_FOUND, ErrorCode.PROJECT_NOT_EXIST.name(), "Project is not exist"));
            if (project.getLeader() != null && project.getLeader().getId().equals(user.getId())) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Error in security expression: {}", e);
        }

        return false;
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

    private String getParam(Object o, int indexParam) {
        String par = (String) o;
        String[] params = par.split(",");
        return params[indexParam];
    }

}
