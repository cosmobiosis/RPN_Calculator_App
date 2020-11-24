package org.ecs160.a1;
import java.util.Hashtable;
import java.util.Arrays;
import java.util.Stack;
import java.util.Vector;

public class ModelLists {
    private Stack<ModelStack> lists;

    public ModelLists(ModelStack initialStackRef) {
        // NORMAL SITUATION
        lists = new Stack<ModelStack>();
        lists.push(initialStackRef);
    }

    public void pushStack(String[] vals) {
        lists.push(new ModelStack(vals));
    }

    public void pushStack(Vector<String> vals) {
        lists.push(new ModelStack(vals));
    }

    public void pushStack() {
        lists.push(new ModelStack());
    }

    public void popStack() {
        if (lists.size() >= 2) {
            // only pops when list have at least two stacks
            lists.pop();
        }
    }

    public void rollUp() {
        if (!lists.empty()) {
            ModelStack bottom = lists.remove(0);
            lists.push(bottom);
        }
    }

    public void rollDown() {
        if (!lists.empty()) {
            ModelStack top = lists.pop();
            lists.add(0, top);
        }
    }

    public void flush() {
        lists = new Stack<ModelStack>();
        lists.push(new ModelStack());
    }

    public void dupTopStack() {
        ModelStack topDup = new ModelStack(lists.peek().getDataClone());
        lists.push(topDup);
    }

    public ModelStack getTopStack() {
        return lists.peek();
    }

//    public Hashtable<String, Vector<String>> getAllData() {
//        Hashtable<String, Vector<String>> idListMapping = new Hashtable<String, Vector<String>>();
//        for (ModelStack list : lists) {
//            Vector<String> vec = new Vector<String>(Arrays.asList(list.getAllStrs()));
//            idListMapping.put(list.getName(), vec);
//        }
//        return idListMapping;
//    }

    public int getSize() {
        return lists.size();
    }
}
