package kr.gaion.ceh.web.response_processing;

import java.util.ArrayList;
import java.util.List;

/**
 * Make data for decision tree visualization
 * 
 * @author hoang
 *
 */
public class DecisionTreeParser {

	/**
	 * to parse tree data for visualization
	 * 
	 * @param debugString
	 * @return
	 */
	public static List<String> parseDecisionTree(String debugString) {
		// return value
		List<String> returnValue = null;

		String[] lines = debugString.split("\n");
		List<List<String>> listTrees = new ArrayList<>();

		int tree_index = -1;
		// index is started from 2 because two first lines is meaningless
		for (int index = 2; index < lines.length; index++) {
			if (lines[index].trim().startsWith("Tree")) {
				listTrees.add(new ArrayList<>());
				tree_index++;
			} else {
				listTrees.get(tree_index).add(lines[index].trim());
			}
		}

		returnValue = new ArrayList<>();

		// iterate all trees, make corresponding data
		for (List<String> tree : listTrees) {
			Node root = makeBinaryTree(tree);
			returnValue.add(convertNodeToString(root).toString());
		}

		return returnValue;
	}

	/**
	 * to make string data for each node<br>
	 * This is recursive function
	 * 
	 * @param node
	 * @return
	 */
	private static StringBuilder convertNodeToString(Node node) {
		StringBuilder dataBuilder = new StringBuilder();

		// if current Node is root, append character "[" to the head of string
		if (node.isRoot()) {
			dataBuilder.append("[");
		} else {
			// continue
		}

		// break recursive condition:
		if (node.isLeaf()) {
			dataBuilder.append("{\"children\":[]");
			dataBuilder.append(",");
			dataBuilder.append("\"name\":\"");
			dataBuilder.append(node.getName());
			if (node.isLeftNode()) {
				dataBuilder.append("\"},");
			} else {
				dataBuilder.append("\"}");
			}

			return dataBuilder;
		}
		// open
		dataBuilder.append("{");

		// children
		dataBuilder.append("\"children\":[");

		// recursive all children
		if (node.getLeft() != null) {
			dataBuilder.append(convertNodeToString(node.getLeft()));
		}
		if (node.getRight() != null) {
			dataBuilder.append(convertNodeToString(node.getRight()));
		}

		dataBuilder.append("],");

		// name
		dataBuilder.append("\"name\":\"");
		dataBuilder.append(node.getName());
		dataBuilder.append("\"");

		// close
		if (node.isRoot()) {
			dataBuilder.append("}");
			dataBuilder.append("]");
		} else if (node.isLeftNode()) {
			dataBuilder.append("},");
		} else {
			dataBuilder.append("}");
		}

		return dataBuilder;
	}

	/**
	 * to make Binary Decision Tree from Spark Random Forest classification
	 * debug string
	 * 
	 * @param lines
	 * @return
	 */
	private static Node makeBinaryTree(List<String> lines) {
		// make the binary tree
		Node root = new Node();
		Node currentNode = root;
		Node childNode = null;
		String name = "";

		for (String line : lines) {
			line = line.trim();
			if (line.length() > 0) {
				name = "";

				if (line.startsWith("If")) {
					name = line.substring(line.indexOf("("), line.length());
					childNode = new Node();
					childNode.setName(name);
					if (currentNode.getLeft() == null) {
						currentNode.setLeft(childNode);
					} else {
						currentNode.setRight(childNode);
					}
					childNode.setParent(currentNode);
					currentNode = childNode;

				} else if (line.startsWith("Else")) {
					if (currentNode.getRight() == null) {
						// do nothing, back to this node and continue
					} else {
						while (currentNode.getRight() != null) {
							currentNode = currentNode.getParent();
						}
					}

				} else if (line.startsWith("Predict:")) {
					// add leaf
					name = line.substring(("Predict:".length() + 1), line.length());
					childNode = new Node();
					childNode.setName(name);
					childNode.setLeaf(true);
					childNode.setParent(currentNode);
					if (currentNode.getLeft() == null) {
						currentNode.setLeft(childNode);
					} else {
						currentNode.setRight(childNode);
					}
				}
			} else {
				// continue
			}
		}

		root = root.getLeft();
		root.setRoot(true);

		return root;
	}
}
