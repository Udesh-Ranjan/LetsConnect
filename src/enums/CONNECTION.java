package enums;

/**
 * @dev :   devpar
 * @date :   05-Jun-2021
 */
public enum CONNECTION {
    DISCONNECT {
        @Override
        public String toString(){
            return "CONNECTION_CLOSE";
        }
    },
    CONNECT{
        @Override
        public String toString(){
            return "CONNECT";
        }
    },
    START_SERVER{
        @Override
        public String toString(){
            return "START_SERVER";
        }
    },
    STOP_SERVER{
        @Override
        public String toString(){
            return "STOP_SERVER";
        }
    },
    CLOSE_SESSION{
      @Override
      public String toString(){
          return "CLOSE_SESSION";
      }
    },
    REQUEST_ACCEPTED,
    REQUEST_REJECTED
}
