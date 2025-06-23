package br.com.lucasmteixeira.playground.game;

public enum GameObjectType {
	NOTHING(0), PERSON(1), GROUND(2);

	private final int categoryCode;

	GameObjectType(int categoryCode) {
		this.categoryCode = categoryCode;
	}

	public int getCategoryCode() {
		return this.categoryCode;
	}
}
