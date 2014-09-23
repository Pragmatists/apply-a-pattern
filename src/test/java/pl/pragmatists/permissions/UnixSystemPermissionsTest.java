package pl.pragmatists.permissions;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.pragmatists.permissions.UnixSystemPermission.*;

public class UnixSystemPermissionsTest {

    private SystemAdmin admin = new SystemAdmin();

    @Test
    public void newPermission_areRequestedByDefault() {
        UnixSystemPermission permission = createPermission();

        assertThat(permission.current()).isEqualTo(REQUESTED);
    }

    private UnixSystemPermission createPermission() {
        return new UnixSystemPermission(new User(), new Profile());
    }

    @Test
    public void requestedPermission_whenGranted_staysRequested() {
        UnixSystemPermission permission = createPermission();

        permission.grantedBy(admin);

        assertThat(permission.current()).isEqualTo(REQUESTED);
        assertThat(permission.isGranted()).isFalse();
    }

    @Test
    public void requestedPermission_whenClaimed_becomesClaimed() {
        UnixSystemPermission permission = createPermission();

        permission.claimedBy(admin);

        assertThat(permission.current()).isEqualTo(CLAIMED);
    }

    @Test
    public void claimedPermission_whenDenied_becomesDenied() {
        UnixSystemPermission permission = createClaimedPermission();

        permission.deniedBy(admin);

        assertThat(permission.current()).isEqualTo(DENIED);
    }

    @Test
    public void claimedPermission_whenGranted_becomesGranted() {
        UnixSystemPermission permission = createClaimedPermission();

        permission.grantedBy(admin);

        assertThat(permission.current()).isEqualTo(GRANTED);
        assertThat(permission.isGranted()).isTrue();
        assertThat(permission.isUnixPermissionGranted()).isFalse();
    }

    @Test
    public void grantedPermission_cannotBeChanged() {
        UnixSystemPermission permission = createGrantedPermission();

        permission.claimedBy(admin);
        permission.deniedBy(admin);

        assertThat(permission.current()).isEqualTo(GRANTED);
        assertThat(permission.isGranted()).isTrue();
    }

    @Test
    public void onlyTheSameAdmin_canGrant() {
        UnixSystemPermission permission = createPermission();
        permission.claimedBy(admin);

        permission.grantedBy(anotherAdmin());

        assertThat(permission.current()).isEqualTo(CLAIMED);
    }

    @Test
    public void onlyTheSameAdmin_canDeny() {
        UnixSystemPermission permission = createPermission();
        permission.claimedBy(admin);

        permission.deniedBy(anotherAdmin());

        assertThat(permission.current()).isEqualTo(CLAIMED);
    }

    @Test
    public void unixPermission_requested() {
        UnixSystemPermission permission = createUnixPermissionRequest();

        permission.claimedBy(admin);
        permission.grantedBy(admin);

        assertThat(permission.current()).isEqualTo(UNIX_REQUESTED);
    }

    @Test
    public void unixPermission_claimed() {
        UnixSystemPermission permission = createUnixPermissionRequest();

        permission.claimedBy(admin);
        permission.grantedBy(admin);
        permission.claimedBy(admin);

        assertThat(permission.current()).isEqualTo(UnixSystemPermission.UNIX_CLAIMED);
        assertThat(permission.isUnixPermissionGranted()).isFalse();
    }

    @Test
    public void unixPermission_claimed_and_granted() {
        UnixSystemPermission permission = createUnixPermissionRequest();

        permission.claimedBy(admin);
        permission.grantedBy(admin);
        permission.claimedBy(admin);
        permission.grantedBy(admin);

        assertThat(permission.current()).isEqualTo(UnixSystemPermission.GRANTED);
        assertThat(permission.isGranted()).isTrue();
        assertThat(permission.isUnixPermissionGranted()).isTrue();
    }

    @Test
    public void unixPermission_claimed_and_denied() {
        UnixSystemPermission permission = createUnixPermissionRequest();

        permission.claimedBy(admin);
        permission.grantedBy(admin);
        permission.claimedBy(admin);
        permission.deniedBy(admin);

        assertThat(permission.current()).isEqualTo(UnixSystemPermission.DENIED);
        assertThat(permission.isGranted()).isFalse();
        assertThat(permission.isUnixPermissionGranted()).isFalse();
    }


    private UnixSystemPermission createUnixPermissionRequest() {
        return new UnixSystemPermission(new User(), new Profile().requireUnixPermission());
    }
    private SystemAdmin anotherAdmin() {
        return new SystemAdmin();
    }

    private UnixSystemPermission createGrantedPermission() {
        UnixSystemPermission permission = createPermission();
        permission.claimedBy(admin);
        permission.grantedBy(admin);
        return permission;
    }

    private UnixSystemPermission createClaimedPermission() {
        UnixSystemPermission permission = createPermission();
        permission.claimedBy(admin);
        return permission;
    }
}
