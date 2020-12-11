public class TrieStringMap {
    public class TrieNode {
        private TrieNode() {}
        private TrieNode(Character character, int depth){
            this.character = character;
            this.depth = depth;
        }

        TrieNode[] Nodes = new TrieNode[R];
        Character character = null;
        String value = null;
        int depth = 0;
    }

    private int R;
    private int Offset;
    TrieNode root;

    public TrieStringMap(){
        R = 26;
        Offset = 'A';
    }

    public TrieStringMap(int R, int Offset){
        this.R = R;
        this.Offset = Offset;
    }

    public TrieNode getRoot(){
        if(root == null)
            root = new TrieNode();

        return root;
    }


    public void add(String key, String value)
    {
        if(key.length() == 0)
            return;
        root = add(null, root, key, value, 0);
    }

    public void add(String key)
    {
        if(key.length() == 0)
            return;
        root = add(null, root, key, key, 0);
    }


    public boolean contains(String key)
    {
        return contains(root, key, 0) != null;
    }

    public TrieNode getNode(TrieNode parent, char character) {
        if(parent == null)
            return null;

        int nextIdx = character - Offset;

        if(nextIdx < 0 || nextIdx >= R)
            return null;

        return parent.Nodes[nextIdx];
    }

    private TrieNode contains(TrieNode node, String key, int d) {
        if(node == null)
            return null;

        if(d == key.length() && d != 0 || key.length() == 0)
        {
            if(node.value != null)
                return node;
            else
                return null;
        }

        int nextIdx = key.charAt(d) - Offset;

        if(nextIdx < 0 || nextIdx >= R)
            return null;

        return contains(node.Nodes[nextIdx], key, d + 1 );
    }

    private TrieNode add(Character character, TrieNode node, String key, String value, int d) {
        if(node == null)
            node = new TrieNode(character, d);

        if(d == key.length() && d != 0)
        {
            node.value = value;
            return node;
        }

        int nextIdx = key.charAt(d) - Offset;

        if(nextIdx < 0 || nextIdx >= R)
            throw new IllegalArgumentException("key contains vales outside of radix range");

        node.Nodes[nextIdx] = add(key.charAt(d), node.Nodes[nextIdx], key, value,d + 1 );

        return node;
    }

    public static void main(String[] args)
    {
        TrieStringMap set = new TrieStringMap();

        set.add("HELLO", "aa");
        set.add("HE", "aa");
        set.add("WO", "aa");
        set.add("", "aa");

        boolean h = set.contains("HA");
        boolean hq = set.contains("HE");
        boolean b = set.contains("HE");
        boolean a = set.contains("W");
        boolean q = set.contains("WO");
        boolean qa = set.contains("WOMAN");
        boolean aqa = set.contains("");

        TrieStringMap unicodeset = new TrieStringMap(256, 0);

        unicodeset.add("Hello1234", "aa");
        unicodeset.add("HE", "aa");
        unicodeset.add("WO", "aa");

        h = unicodeset.contains("HA");
        hq = unicodeset.contains("HE");
        b = unicodeset.contains("Hello1234");
        a = unicodeset.contains("W");
        q = unicodeset.contains("WO");
        qa = unicodeset.contains("WOMAN");

    }
}
