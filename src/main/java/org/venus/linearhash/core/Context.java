package org.venus.linearhash.core;

import lombok.Getter;
import org.venus.linearhash.store.OverflowPool;

/**
 * @Author venus
 * @Date 2024/8/7
 * @Version 1.0
 */
@Getter
public class Context<KT, VT> {

    private static final Context<?, ?> instance = new Context<>();

    @SuppressWarnings("unchecked")
    public static <KT, VT> Context<KT, VT> getInstance() {
        return (Context<KT, VT>) instance;
    }

    private final OverflowPool<KT, VT> overflowPool = new OverflowPool<>();

}
