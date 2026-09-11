package com.dockmind.infrastructure.git;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class GitService {
    public Path cloneRepository(String gitUrl, String branch) throws IOException, GitAPIException {
        //create temporay directory
        Path tempDirectory = Files.createTempDirectory("dockmind-repo-");

        //clone in that tempDirectory
        CloneCommand cloneCommand = Git.cloneRepository().setURI(gitUrl)
                .setDirectory(tempDirectory.toFile())
                .setCloneAllBranches(true);
        //branch check
        if (branch != null || !branch.isBlank()) {
            cloneCommand.setBranch(branch);
        }
        // execute
        try (Git git = cloneCommand.call()) {
        }

        return tempDirectory;
    }
}
