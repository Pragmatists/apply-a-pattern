package pl.pragmatists.permissions;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SystemPermissionsTest {

    private SystemAdmin admin = new SystemAdmin();

    @Test
    public void newPermission_areRequestedByDefault() {
        SystemPermission permission = createPermission();

        assertThat(permission.current()).isEqualTo(SystemPermission.REQUESTED);
    }

    private SystemPermission createPermission() {
        return new SystemPermission(new User());
    }

    @Test
    public void requestedPermission_whenGranted_staysRequested() {
        SystemPermission permission = createPermission();

        permission.grantedBy(admin);

        assertThat(permission.current()).isEqualTo(SystemPermission.REQUESTED);
        assertThat(permission.isGranted()).isFalse();
    }

    @Test
    public void requestedPermission_whenClaimed_becomesClaimed() {
        SystemPermission permission = createPermission();

        permission.claimedBy(admin);

        assertThat(permission.current()).isEqualTo(SystemPermission.CLAIMED);
    }

    @Test
    public void claimedPermission_whenDenied_becomesDenied() {
        SystemPermission permission = createClaimedPermission();

        permission.deniedBy(admin);

        assertThat(permission.current()).isEqualTo(SystemPermission.DENIED);
    }

    @Test
    public void claimedPermission_whenGranted_becomesGranted() {
        SystemPermission permission = createClaimedPermission();

        permission.grantedBy(admin);

        assertThat(permission.current()).isEqualTo(SystemPermission.GRANTED);
        assertThat(permission.isGranted()).isTrue();
    }

    @Test
    public void grantedPermission_cannotBeChanged() {
        SystemPermission permission = createGrantedPermission();

        permission.claimedBy(admin);
        permission.deniedBy(admin);

        assertThat(permission.current()).isEqualTo(SystemPermission.GRANTED);
        assertThat(permission.isGranted()).isTrue();
    }

    @Test
    public void onlyTheSameAdmin_canGrant() {
        SystemPermission permission = createPermission();
        permission.claimedBy(admin);

        permission.grantedBy(anotherAdmin());

        assertThat(permission.current()).isEqualTo(SystemPermission.CLAIMED);
    }

    @Test
    public void onlyTheSameAdmin_canDeny() {
        SystemPermission permission = createPermission();
        permission.claimedBy(admin);

        permission.deniedBy(anotherAdmin());

        assertThat(permission.current()).isEqualTo(SystemPermission.CLAIMED);
    }

    private SystemAdmin anotherAdmin() {
        return new SystemAdmin();
    }

    private SystemPermission createGrantedPermission() {
        SystemPermission permission = createPermission();
        permission.claimedBy(admin);
        permission.grantedBy(admin);
        return permission;
    }

    private SystemPermission createClaimedPermission() {
        SystemPermission permission = createPermission();
        permission.claimedBy(admin);
        return permission;
    }
}
