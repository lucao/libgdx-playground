package br.com.lucasmteixeira.playground.game;

public enum CollisionType {
	NOTHING(0), PERSON(1), GROUND(2);

	private final int categoryCode;

	CollisionType(int categoryCode) {
		this.categoryCode = categoryCode;
	}

	public int getCategoryCode() {
		return this.categoryCode;
	}
}
