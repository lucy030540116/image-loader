package com.novoda.pxfetcher;

import com.novoda.pxfetcher.task.Failure;
import com.novoda.pxfetcher.task.Result;
import com.novoda.pxfetcher.task.Retriever;
import com.novoda.pxfetcher.task.RetrieverAsyncTask;
import com.novoda.pxfetcher.task.Success;
import com.novoda.pxfetcher.task.TagWrapper;

public class BitmapLoader {

    private final Retriever retriever;

    public BitmapLoader(Retriever retriever) {
        this.retriever = retriever;
    }

    public void load(TagWrapper tagWrapper, Callback callback) {
        callback.onStart();
        new RetrieverAsyncTask(tagWrapper, retriever)
                .setListener(createListener(callback))
                .executeOnExecutor(DropExecutor.THREAD_POOL_EXECUTOR);
    }

    private RetrieverAsyncTask.Listener createListener(final Callback callback) {
        return new RetrieverAsyncTask.Listener() {
            @Override
            public void onResult(Result result) {
                result.poke(callback);
            }
        };
    }

    public interface Callback {
        void onStart();

        void onResult(Success ok);

        void onResult(Failure ko);
    }

}
