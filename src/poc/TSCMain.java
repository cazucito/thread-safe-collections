/**
 * 
 */
package poc;

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
		CopyOnWriteArrayListDemo.testConstructores();
		CopyOnWriteArrayListDemo.testCopyOnWriteArrayList();
		// CopyOnWriteArraySet
		CopyOnWriteArraySetDemo.testConstructores();
		CopyOnWriteArraySetDemo.testCopyOnWriteArraySet();

	}

}
