# CoreMedia CMCC Shared Lock

[Spring Framework][spring] LockRegistry implementation to support leader
election in a set of [CoreMedia Content Cloud (CMCC)][cmcc] components of the
same type. Thus, having singleton sub-services in automatically scalable
instances, like e.g. the Studio Server.


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

The intended usage is, to inject this library with the component holding
the singleton sub-service via [Spring Framework][spring] `@Configuration`
classes, and manually protect the sub-service core methods with locks.

### Dependency Coordinates

Tagged versions of the library will be published to Maven Central with
`groupId` `de.provocon.coremedia` and `artifactId` `shared-lock`.

```
  <dependencies>
    ....
    <dependency>
      <groupId>de.provocon.coremedia</groupId>
      <artifactId>shared-lock</artifactId>
    </dependency>
    ....
  </dependencies>
```

Snapshot versions are available via the local repositories of the respective
GIT service you are using

```
  <repository>
    <id>provocon.shared-local.snapshots</id>
    <name>Shared Lock Snapshots via Codeberg</name>
    <url>https://codeberg.org/api/packages/provocon/maven</url>
  </repository>
```

```
  <repository>
    <id>provocon.shared-local.snapshots</id>
    <name>Shared Lock Snapshots via GitLab</name>
    <url>https://gitlab.com/api/v4/projects/80628178/packages/maven</url>
  </repository>
```

```
  <repository>
    <id>provocon.shared-local.snapshots</id>
    <name>Shared Lock Snapshots via GitHub</name>
    <url>https://maven.pkg.github.com/provocon/coremedia-shared-lock</url>
  </repository>
```

### Integration into the Workspace

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


[issues]:   https://codeberg.org/provocon/coremedia-shared-lock/issues
[codeberg]: https://codeberg.org/provocon/coremedia-shared-lock
[gitlab]:   https://gitlab.com/provocon/coremedia-shared-lock
[github]:   https://github.com/provocon/coremedia-shared-lock
[spring]:   https://spring.io/projects/spring-framework
[cmcc]:     https://documentation.coremedia.com/