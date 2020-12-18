public class CircularString{
    private char[] charArray;
    private int startOfString;

    CircularString(char[] charArray, int startOfString)
    {
        this.charArray = charArray;
        this.startOfString = startOfString;
    }

    public int charAt(int idx) // 0 to len -1
    {
        if(startOfString + idx >= charArray.length)
        {
            int newIdx = startOfString + idx - charArray.length;

            if(newIdx < charArray.length)
                return charArray[newIdx];
            else
                return -1;
        }

        return charArray[startOfString + idx];
    }

    public int getStartOfString(){
        return startOfString;
    }
}