package pl.pragmatists.permissions;

public class SystemPermission {

    public static final String REQUESTED = "Requested";
    public static final String CLAIMED = "Claimed";
    public static final String DENIED = "Denied";
    public static final String GRANTED = "Granted";
    private User user;
    private String current = REQUESTED;
    private SystemAdmin admin;
    private boolean granted;

    public SystemPermission(User user) {
        this.user = user;
    }

    public String current() {
        return current;
    }

    public void grantedBy(SystemAdmin admin) {
        if (!current.equals(CLAIMED)) {
            return;
        }
        if (!admin.equals(this.admin)) {
            return;
        }
        current = GRANTED;
        granted = true;
        notifyUserOfPermissionGranted();
    }

    private void notifyUserOfPermissionGranted() {

    }

    public void claimedBy(SystemAdmin admin) {
        if (!current.equals(REQUESTED)) {
            return;
        }
        willBeHandledBy(admin);
        current = CLAIMED;
    }

    private void willBeHandledBy(SystemAdmin admin) {
        this.admin = admin;
    }

    public void deniedBy(SystemAdmin admin) {
        if (!current.equals(CLAIMED)) {
            return;
        }
        if (!admin.equals(this.admin)) {
            return;
        }
        current = DENIED;
        granted = false;
    }


    public boolean isGranted() {
        return granted;
    }
}
