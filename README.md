# CoreMedia CMCC Shared Lock

Spring LockRegistry implementation to support leader election in a set of
CoreMedia Content Cloud (CMCC) components of the same type. Thus, having
singleton sub-services in automatically scalable instances, like e.g. the Studio
Server.


## Feedback

Feel invited to use the [issues][issues] section of this repository at
[Codeberg][codeberg] for feedback.


## Compatibility

CoreMedia Blueprints Content Cloud CMCC-13 and CMCC-12 are currently supported.

This library can be integrated with blueprints workspaces, at least up to
platform version 2512.


## Availability

The home of this extension is the repository at [Codeberg][codeberg] with
mirrors at [GitHub][github] and [GitLab][gitlab]. When possible, please prefer
references to [Codeberg][codeberg].


## Usage

When integrated with its configuration, this library can e.g. facilitate a
singleton sub-service in a scaled component with a code pattern like the
following.

```
    private static final String MY_SUB_SERVICE_ID = "my-service";

    private final LockRegistry lockRegistry;


    public CmccContentRepositoryLockedService(LockRegistry lockRegistry) {
        this.lockRegistry = lockRegistry;
    }


    public void lockedSectionDemo() throws InterruptedException {
        Lock lock = lockRegistry.obtain(MY_SUB_SERVICE_ID);
        boolean lockAquired = lock.tryLock();
        if (lockAquired) {
            try {
                LOG.info("singleton sub-service within scaled component.");
                Thread.sleep(30000);
            } finally {
                lock.unlock();
            }
        }
    }
```


[issues]: https://codeberg.org/provocon/coremedia-shared-lock/issues
[codeberg]: https://codeberg.org/provocon/coremedia-shared-lock
[gitlab]: https://gitlab.com/provocon/coremedia-shared-lock
[github]: https://github.com/provocon/coremedia-shared-lock
