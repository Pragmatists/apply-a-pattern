package pl.pragmatists.permissions;

public class Profile {
    public boolean requireUnixPermission;

    public Profile requireUnixPermission() {
        requireUnixPermission = true;
        return this;
    }
}
