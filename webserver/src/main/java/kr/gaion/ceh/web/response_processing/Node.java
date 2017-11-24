package kr.gaion.ceh.web.response_processing;

/**
 * this class to build binary tree
 * 
 * @author hoang
 *
 */
public class Node {
	private boolean isRoot;
	private boolean isLeaf;
	private boolean isLeftNode;
	private boolean isRightNode;
	private String name;
	private Node right;
	private Node left;
	private Node parent;

	/**
	 * Constructor
	 */
	public Node() {
		parent = null;
		left = null;
		right = null;
		name = null;
		isLeaf = false;
		isLeftNode = false;
		isRightNode = false;
	}

	/*
	 * Getters and Setters
	 */
	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		right.setRightNode(true);
		this.right = right;
	}

	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		left.setLeftNode(true);
		this.left = left;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public boolean isLeftNode() {
		return isLeftNode;
	}

	public void setLeftNode(boolean isLeftNode) {
		this.isLeftNode = isLeftNode;
	}

	public boolean isRightNode() {
		return isRightNode;
	}

	public void setRightNode(boolean isRightNode) {
		this.isRightNode = isRightNode;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

}
