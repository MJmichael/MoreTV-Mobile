package yang.mobile.Utils;

import android.util.Pair;

import java.util.List;

/**
 * Created by yang on 15/3/28.
 */
public class ListUtils {

    public static boolean isEmpty(List list) {
        return (list == null || list.isEmpty());
    }

    public static int size(List list) {
        return isEmpty(list) ? 0 : list.size();
    }

    public static <T> boolean fillPairList(List<T> list,
                                   List<Pair<T, T>> pairList) {
        if(isEmpty(list) || pairList == null) {
            return false;
        }
        pairList.clear();

        int length = list.size();

        T tempInfo = null;
        for (int i = 0; i < length; i++) {
            T tempInfo2 = null;
            if (i % 2 == 0) {
                tempInfo = list.get(i);
                i++;
            }

            if (i < length) {
                tempInfo2 = list.get(i);
            }
            pairList.add(Pair.create(tempInfo, tempInfo2));
        }

        return true;
    }
}
