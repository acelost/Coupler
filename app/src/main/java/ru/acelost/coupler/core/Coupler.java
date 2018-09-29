package ru.acelost.coupler.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Coupler {

    private static final int NO_POSIION = -1;

    private List<AnchorItem> mContent = new ArrayList<>();

    private List<SourceHolder> mSources = new ArrayList<>();

    public Coupler() {

    }

    public void requestBefore(@NonNull Anchor anchor, int count) {

    }

    @WorkerThread
    public void requestAfter(@NonNull Anchor anchor, int count) {
        Collections.unmodifiableList()
        SourceHolder holder = anchor.getHasAfter()
                ? findSource(anchor.getSourceId())
                : findSourceAfter(anchor.getSourceId());
        if (holder == null) {
            throw new RuntimeException("No data after " + anchor);
        }
        List<AnchorItem> result = new ArrayList<>(count);
        while (holder != null && count > 0) {
            // todo если сменили источник, не передаем якорь
            final List<AnchorItem> portion = holder.source.loadAfter(anchor, count);
            if (portion.isEmpty()) {
                anchor.hasAfter = false;
            } else {
                // Add portion to result list
                result.addAll(portion);
                // Recalculate request params
                count -= portion.size();
                anchor = portion.get(portion.size() - 1).getAnchor();
            }
            // Find next source
            holder = findSourceAfter(holder.sourceId);
        }
        if (result.isEmpty()) {
            // No data after, fix anchor flag
            anchor.hasAfter = false;
        }
    }

    public void requestBetween(@NonNull Anchor anchorAfter, @NonNull Anchor anchorBefore, int count) {

    }

    public void requestReplace(@NonNull Anchor anchorSince, @NonNull Anchor anchorUntil, int count) {

    }

    private int findPosition(@NonNull Anchor anchor) {
        for (int i = 0, size = mContent.size(); i < size; ++i) {
            final AnchorItem item = mContent.get(i);
            if (item.getAnchor().equals(anchor)) {
                return i;
            }
        }
        return NO_POSIION;
    }

    @Nullable
    private SourceHolder findSource(@NonNull String sourceId) {
        for (int i = 0, size = mSources.size(); i < size; ++i) {
            final SourceHolder holder = mSources.get(i);
            if (holder.sourceId.equals(sourceId)) {
                return holder;
            }
        }
        return null;
    }

    @Nullable
    private SourceHolder findSourceAfter(@NonNull String sourceId) {
        boolean anchorFound = false;
        for (int i = 0, size = mSources.size(); i < size; ++i) {
            final SourceHolder holder = mSources.get(i);
            if (anchorFound) {
                return holder;
            }
            if (holder.sourceId.equals(sourceId)) {
                anchorFound = true;
            }
        }
        return null;
    }

    @Nullable
    private SourceHolder findSourceBefore(@NonNull String sourceId) {
        boolean anchorFound = false;
        for (int i = mSources.size() - 1; i > -1; --i) {
            final SourceHolder holder = mSources.get(i);
            if (anchorFound) {
                return holder;
            }
            if (holder.sourceId.equals(sourceId)) {
                anchorFound = true;
            }
        }
        return null;
    }

    private static class SourceHolder {

        @NonNull final String sourceId;
        @NonNull final DataSource source;

        SourceHolder(@NonNull String sourceId, @NonNull DataSource source) {
            this.sourceId = sourceId;
            this.source = source;
        }

        @NonNull
        @Override
        public String toString() {
            return "SourceHolder[id = " + sourceId + ", source = " + source;
        }

    }

    public interface DataSource {

        @NonNull
        List<AnchorItem> loadAfter(@NonNull Anchor anchor, int count);

    }

}
