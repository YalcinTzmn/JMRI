package jmri.implementation;

import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;
import jmri.profile.Profile;
import jmri.profile.ProfileUtils;
import jmri.spi.InitializationException;
import jmri.spi.PreferencesProvider;
import jmri.util.FileUtil;

/**
 *
 * @author Randall Wood (C) 2015
 */
public class FileLocationsPreferences implements PreferencesProvider {

    private final HashMap<Profile, Boolean> initialized = new HashMap<>();
    public static final String USER_FILES = "user-files"; // NOI18N
    public static final String SCRIPTS = "scripts"; // NOI18N

    @Override
    public void initialize(Profile profile) throws InitializationException {
        Preferences shared = ProfileUtils.getPreferences(profile, this.getClass(), true);
        Preferences perNode = ProfileUtils.getPreferences(profile, this.getClass(), false);
        String userFiles = shared.get(USER_FILES, FileUtil.PROFILE);
        if (!userFiles.startsWith(FileUtil.PROFILE)) {
            userFiles = perNode.get(USER_FILES, userFiles);
        }
        FileUtil.setUserFilesPath(FileUtil.getAbsoluteFilename(userFiles));
        String scripts = shared.get(SCRIPTS, FileUtil.PROFILE);
        if (!scripts.startsWith(FileUtil.PROFILE) && !scripts.startsWith(FileUtil.PROGRAM)) {
            scripts = perNode.get(SCRIPTS, scripts);
        }
        FileUtil.setScriptsPath(FileUtil.getAbsoluteFilename(scripts));
        this.initialized.put(profile, Boolean.TRUE);
        try {
            if (!FileUtil.getFile(userFiles).isDirectory()) {
                String message = "User files location \"{0}\" is not a directory.";
                userFiles = FileUtil.getAbsoluteFilename(userFiles);
                throw new InitializationException(MessageFormat.format(message, userFiles), Bundle.getMessage(message, userFiles));
            }
        } catch (FileNotFoundException ex) {
            String message = "User files location \"{0}\" does not exist.";
            userFiles = FileUtil.getAbsoluteFilename(userFiles);
            throw new InitializationException(MessageFormat.format(message, userFiles), Bundle.getMessage(message, userFiles));
        }
        try {
            if (!FileUtil.getFile(scripts).isDirectory()) {
                String message = "Scripts location \"{0}\" is not a directory.";
                scripts = FileUtil.getAbsoluteFilename(scripts);
                throw new InitializationException(MessageFormat.format(message, scripts), Bundle.getMessage(message, scripts));
            }
        } catch (FileNotFoundException ex) {
            String message = "Scripts location \"{0}\" does not exist.";
            scripts = FileUtil.getAbsoluteFilename(scripts);
            throw new InitializationException(MessageFormat.format(message, scripts), Bundle.getMessage(message, scripts));
        }
    }

    @Override
    public boolean isInitialized(Profile profile) {
        return this.initialized.getOrDefault(profile, false);
    }

    @Override
    public Iterable<Class<? extends PreferencesProvider>> getRequires() {
        return new ArrayList<>();
    }

    @Override
    public Iterable<Class<?>> getProvides() {
        ArrayList<Class<?>> provides = new ArrayList<>();
        provides.add(FileLocationsPreferences.class);
        return provides;
    }

    @Override
    public void savePreferences(Profile profile) {
        Preferences shared = ProfileUtils.getPreferences(profile, this.getClass(), true);
        Preferences perNode = ProfileUtils.getPreferences(profile, this.getClass(), false);
        shared.put(USER_FILES, FileUtil.getPortableFilename(FileUtil.getUserFilesPath(), true, false));
        shared.put(SCRIPTS, FileUtil.getPortableFilename(FileUtil.getScriptsPath()));
        perNode.put(USER_FILES, FileUtil.getPortableFilename(FileUtil.getUserFilesPath(), true, false));
        perNode.put(SCRIPTS, FileUtil.getPortableFilename(FileUtil.getScriptsPath()));
    }

}
