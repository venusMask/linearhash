package org.venus.linearhash;

import org.venus.linearhash.args.Argument;
import org.venus.linearhash.core.Context;
import org.venus.linearhash.core.LinearHash;
import org.venus.linearhash.store.HashFunction;

/**
 * @Author venus
 * @Date 2024/8/8
 * @Version 1.0
 */
public class App {

    public static void main(String[] args) {
        Argument argument = new Argument();
        argument.parse(args);
        argument.parseConfig();
        HashFunction<String> hf = Object::hashCode;
        Context<String, String> context = Context.getInstance();
        context.setHashFunction(hf);
        LinearHash<String, String> table = new LinearHash<>();
    }

}
