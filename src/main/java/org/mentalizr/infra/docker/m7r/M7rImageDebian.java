package org.mentalizr.infra.docker.m7r;

import org.mentalizr.infra.Const;

public class M7rImageDebian {

    public static boolean exists() {
        return M7rImage.exists(Const.IMAGE_DEBIAN);
    }

    public static void build() {
        M7rImage.build(Const.IMAGE_DEBIAN, Const.IMAGE_DEBIAN_URL);
    }

    public static void remove() {
        M7rImage.remove(Const.IMAGE_DEBIAN);
    }

    public static void createBackupTag() {
        String destTaggedImageName = BackupTags.getNameWithBackupTag(Const.IMAGE_DEBIAN);
        M7rImage.tag(Const.IMAGE_DEBIAN, destTaggedImageName);
    }

}
