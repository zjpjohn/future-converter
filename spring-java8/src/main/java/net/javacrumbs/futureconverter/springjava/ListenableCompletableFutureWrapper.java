/**
 * Copyright 2009-2015 the original author or authors.
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
package net.javacrumbs.futureconverter.springjava;

import net.javacrumbs.futureconverter.common.spring.AbstractSpringListenableFutureWrapper;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Wraps {@link java.util.concurrent.CompletableFuture} and provides {@link org.springframework.util.concurrent.ListenableFuture} interface.
 *
 * @param <T>
 */
class ListenableCompletableFutureWrapper<T> extends AbstractSpringListenableFutureWrapper<T> implements ListenableFuture<T> {
    ListenableCompletableFutureWrapper(CompletableFuture<T> wrapped) {
        super(wrapped);
        wrapped.whenComplete((result, ex) -> {
            if (ex != null) {
                if (ex instanceof CompletionException && ex.getCause() != null) {
                    failure(ex.getCause());
                } else {
                   failure(ex);
                }
            } else {
                success(result);
            }
        });
    }

    @Override
    public CompletableFuture<T> getWrappedFuture() {
        return (CompletableFuture<T>) super.getWrappedFuture();
    }
}
