/**
 * 
 */
package poc;

import poc.cc.ConcurrentSkipListMapDemo;
import poc.cc.ConcurrentSkipListSetDemo;
import poc.cc.CopyOnWriteArrayListDemo;
import poc.cc.CopyOnWriteArraySetDemo;

/**
 * @author cazucito
 *
 */
public class TSCMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// CopyOnWriteArrayList
//		CopyOnWriteArrayListDemo.testConstructores();
//		CopyOnWriteArrayListDemo.testCopyOnWriteArrayList();
//		// CopyOnWriteArraySet
//		CopyOnWriteArraySetDemo.testConstructores();
//		CopyOnWriteArraySetDemo.testCopyOnWriteArraySet();
//		// ConcurrentSkipListSet
//		ConcurrentSkipListSetDemo.testConstructores();
//		ConcurrentSkipListSetDemo.testConcurrentSkipListSet();
		// ConcurrentSkipListMapDemo
		ConcurrentSkipListMapDemo.testConstructores();
		ConcurrentSkipListMapDemo.testConcurrentSkipListMap();
	}

}
