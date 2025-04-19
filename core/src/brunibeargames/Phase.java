package brunibeargames;

public enum Phase {
	CARD_CLEANUP(),
	DETERMINE_INITIATIVE(),
	PLAYER1_CARD(),
	PLAYER2_CARD(),
	DETERMINE_COMMAND(),
	DO_COMMAND(),
	DO_COMMAND_RANDOM(),
	DO_COMMAND_DIVISION(),
	MOVEMENT(),
	BARRAGE(),
	COMBAT(),
	END_COMMANDER(),
	END_DETERMINE(),
	NEXT_TURN(),
	;

	/**
	 * each doSeaMove is associated with a side
	 */
		private boolean isAlliedPhase;
		private boolean isNonePhase;



		Phase(boolean isAllies){
			isAlliedPhase = isAllies;
			isNonePhase =false;
		}
		Phase(){
			isAlliedPhase = false;
			isNonePhase =true;
		}
		public boolean isAllied() {
			return isAlliedPhase;

		}

}  

 