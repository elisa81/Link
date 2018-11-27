package com.womenproiot.www.link;

public class LinkDTO {
    class Place {
        public String name;
        public String roadAddress;
        public String jibunAddress;
        public String phoneNumber;
        public double latitude;
        public double longitude;
        public double distance;
        public String sessionId;

        public Place(String name, String roadAddress, String jibunAddress, String phoneNumber,
                     double latitude, double longitude, double distance, String sessionId) {
            this.name = name;
            this.roadAddress = roadAddress;
            this.jibunAddress = jibunAddress;
            this.phoneNumber = phoneNumber;
            this.latitude = latitude;
            this.longitude = longitude;
            this.distance = distance;
            this.sessionId = sessionId;
        }
    }
}
