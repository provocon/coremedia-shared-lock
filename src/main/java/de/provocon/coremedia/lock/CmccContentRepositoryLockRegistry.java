/*
 * Copyright 2026 Martin Goellnitz for Provocon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.provocon.coremedia.lock;

import com.coremedia.cap.content.ContentRepository;
import com.coremedia.cap.content.PropertyService;
import java.util.concurrent.locks.Lock;
import org.springframework.integration.support.locks.LockRegistry;


/**
 * LockRegistry implementation intended for the Leader Election within a set
 * of components from the CoreMedia Content Cloud of the same type, like e.g.
 * Studio Server. It is based on the Property Service of the Content Repository
 * and must therefore not be used for a tremendous amount of locks, which
 * for the given scenario should not be the case anyway.
 */
public class CmccContentRepositoryLockRegistry implements LockRegistry {

    private static final long DEFAULT_LOCKING_TIME = 60*60; // one hour

    private final PropertyService propertyService;

    private final long lockingTime;


    /**
     * Create Lock Registry instance for the given repository with the given
     * locking time.
     * @param repository CoreMedia Content Repository instance
     * @param lockingTimeSeconds seconds to hold this lock until it expires
     */
    public CmccContentRepositoryLockRegistry(ContentRepository repository, long lockingTimeSeconds) {
        this.propertyService = repository.getPropertyService();
        this.lockingTime = lockingTimeSeconds;
    }


    /**
     * Create Lock Registry instance for the given repository with the default
     * locking time.
     * @param repository CoreMedia Content Repository instance
     */
    public CmccContentRepositoryLockRegistry(ContentRepository repository) {
        this(repository, DEFAULT_LOCKING_TIME);
    }


    @Override
    public Lock obtain(Object key) {
        return new CmccContentRepositoryLock(propertyService, key.toString(), lockingTime);
    }

}
