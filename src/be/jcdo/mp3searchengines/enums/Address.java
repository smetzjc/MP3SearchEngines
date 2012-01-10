package be.jcdo.mp3searchengines.enums;

/**
 *
 * @author Hervé
 */
public enum Address {
    MRTZADDR {
        @Override
        public String toString() {
            return "http://www.mrtzcmp3.net/";
        }
    },

    YOUTUBEADDR {
        @Override
        public String toString() {
            return "http://www.youtube.com/";
        }
    },
    
    AUDIODUMPADDR {
        @Override
        public String toString() {
            return "http://www.audiodump.com/";
        }
    },
    
    WRZUTAADDR {
    	@Override
        public String toString() {
            return "http://www.wrzuta.pl/";
        }
    }
}