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

import com.coremedia.cap.content.PropertyService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.apache.commons.lang3.StringUtils;


/**
 * Lock based on the Property Service of the CoreMedia Content Cloud
 * Content Repository. Must therefore not be used for a tremendous amount
 * of locks.
 */
/* default */ class CmccContentRepositoryLock implements Lock {

    private static final long WAITING_INTERVALL_MS = 30000;

    private static final String CLASS_NAME = CmccContentRepositoryLock.class.getSimpleName();

    private final PropertyService propertyService;

    private final String lockKey;

    private final long lockingTime;


    /* default */ CmccContentRepositoryLock(PropertyService propertyService, String lockKey, long lockingTimeSeconds) {
        this.propertyService = propertyService;
        this.lockKey = "Shared_Lock_Key_"+lockKey;
        this.lockingTime = lockingTimeSeconds;
    }


    private String findLock(String key) {
        String lock = propertyService.get(key);
        if (StringUtils.isEmpty(lock)) {
            lock = "0";
        }
        return lock;
    }


    @Override
    public void lock() {
        while (!tryLock()) {
            try {
                Thread.sleep(WAITING_INTERVALL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }


    @Override
    public void lockInterruptibly() throws InterruptedException {
        while (!tryLock()) {
            if (Thread.interrupted()) {
                throw new InterruptedException(CLASS_NAME+" "+lockKey+" interrupted.");
            }
            Thread.sleep(WAITING_INTERVALL_MS);
        }
    }


    @Override
    public boolean tryLock() {
        boolean result = false;
        synchronized (propertyService) {
            String lock = findLock(lockKey);
            long lockTime = Long.parseLong(lock);
            long now = System.currentTimeMillis()/1000;
            long age = (now-lockTime);
            if (age>lockingTime) {
                propertyService.put(lockKey, ""+now);
                result = true;
            }
        }
        return result;
    }


    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long deadline = System.currentTimeMillis()+unit.toMillis(time);
        while (System.currentTimeMillis()<deadline) {
            if (tryLock()) {
                return true;
            }
            Thread.sleep(WAITING_INTERVALL_MS);
        }
        return false;
    }


    @Override
    public void unlock() {
        synchronized (propertyService) {
            String lock = propertyService.get(lockKey);
            if (StringUtils.isEmpty(lock)) {
                propertyService.remove(lockKey);
            }
        }
    }


    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
