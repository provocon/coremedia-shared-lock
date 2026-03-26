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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.locks.LockRegistry;


/**
 * Spring configuration as code for shared lock with default locking time.
 */
@Configuration
public class CmccContentRepositoryLockConfiguration {

    /**
     * Configuration provides no-arts constructor for annotation based usage.
     */
    public CmccContentRepositoryLockConfiguration() {
        // Nothing to do in here.
    }


    /**
     * Obtain lock registry with injected repository.
     *
     * @param repository CoreMedia CMCC Content Repository taken from configuration elsewhere
     * @return CMCC Content Repository based shared lock
     */
    @Bean
    public LockRegistry cmccContentRepoLockRegistry(ContentRepository repository) {
        return new CmccContentRepositoryLockRegistry(repository);
    }

}
