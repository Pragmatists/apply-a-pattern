package pl.pragmatists.permissions;

public class UnixSystemPermission {

    public static final String REQUESTED = "Requested";
    public static final String CLAIMED = "Claimed";
    public static final String DENIED = "Denied";
    public static final String GRANTED = "Granted";
    public static final String UNIX_REQUESTED = "UnixRequested";
    public static final String UNIX_CLAIMED = "UnixClaimed";

    private User user;
    private Profile profile;
    private String current = REQUESTED;
    private SystemAdmin admin;
    private boolean granted;
    private boolean unixPermissionGranted;

    public UnixSystemPermission(User user, Profile profile) {
        this.user = user;
        this.profile = profile;
    }

    public String current() {
        return current;
    }

    public void grantedBy(SystemAdmin admin) {
        if (!current.equals(CLAIMED) && !current.equals(UNIX_CLAIMED)) {
            return;
        }
        if (!admin.equals(this.admin)) {
            return;
        }
        if (profile.requireUnixPermission && current.equals(UNIX_CLAIMED)) {
            unixPermissionGranted = true;
        } else if (profile.requireUnixPermission && !isUnixPermissionGranted() ) {
            current = UNIX_REQUESTED;
            return;
        }
        current = GRANTED;
        granted = true;
        notifyUserOfPermissionGranted();
    }

    private void notifyUserOfPermissionGranted() {

    }

    public void claimedBy(SystemAdmin admin) {
        if (!current.equals(REQUESTED) && !current.equals(UNIX_REQUESTED)) {
            return;
        }
        willBeHandledBy(admin);
        if (current.equals(REQUESTED)) {
            current = CLAIMED;
        }
        else {
            current = UNIX_CLAIMED;
        }
    }

    private void willBeHandledBy(SystemAdmin admin) {
        this.admin = admin;
    }

    public void deniedBy(SystemAdmin admin) {
        if (!current.equals(CLAIMED) && !current.equals(UNIX_CLAIMED)) {
            return;
        }
        if (!admin.equals(this.admin)) {
            return;
        }
        current = DENIED;
        unixPermissionGranted = false;
        granted = false;
    }


    public boolean isGranted() {
        return granted;
    }

    public boolean isUnixPermissionGranted() {
        return unixPermissionGranted;
    }
}
