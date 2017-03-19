package com.nms.plugin;

import java.util.Map;

/**
 * 插件消息包装类
 * Created by sam on 17-3-13.
 */
public final class PluginMessage {

    public static class Mount extends PluginImpl {

        public Mount(final String guid,final String id, final String entry,final Integer version) {
            super(id, entry,version, null, null);
        }

        public <T extends Plugin> Mount(final T plugin)
        {
            super(plugin.getId(),plugin.getEntry(),plugin.getVersion(),plugin.getAttribute(),plugin.getDescription());
        }

        public Mount(final String guid, final String id, final String entry, final Integer version, final Map<String,Object> attribute, final String description) {
            super(id, entry,version, attribute, description);
        }
    }

    public static class Umount extends PluginImpl {
        public Umount(final String guid, final String id, final String entry, final Integer version) {
            super(id, entry,version, null, null);
        }

        public <T extends Plugin> Umount(final T plugin)
        {
            super(plugin.getId(),plugin.getEntry(),plugin.getVersion(),plugin.getAttribute(),plugin.getDescription());
        }

        public Umount(final String guid, final String id, final String entry, final Integer version, final Map<String,Object> attribute, final String description) {
            super(id, entry,version, attribute, description);
        }
    }

    public static class Remount extends PluginImpl {
        public Remount(final String guid,final String id, final String entry,final Integer version) {
            super(id, entry,version, null, null);
        }

        public <T extends Plugin> Remount(final T plugin)
        {
            super(plugin.getId(),plugin.getEntry(),plugin.getVersion(),plugin.getAttribute(),plugin.getDescription());
        }

        public Remount(final String guid,final String id, final String entry,final Integer version, final Map<String,Object> attribute, final String description) {
            super(id, entry,version, attribute, description);
        }
    }


    public static class MountAll{}
    public static class UmountAll{}
    public static class RemountAll{}


    public static final MountAll mountAll = new MountAll();
    public static final UmountAll umountAll = new UmountAll();
    public static final RemountAll remountAll = new RemountAll();


}
