package com.direwolf20.buildinggadgets.common.tainted.inventory.handle;

import com.direwolf20.buildinggadgets.common.tainted.inventory.materials.objects.IUniqueObject;

public interface IObjectHandle<T> {
    Class<T> getIndexClass();

    T getIndexObject();

    int match(IUniqueObject<?> item, int count, boolean simulate);

    int insert(IUniqueObject<?> item, int count, boolean simulate);

    boolean shouldCleanup();
}
