type LinearGradient = {
    "startX": number,
    "startY": number,
    "endX": number,
    "endY": number,
    "proportional": boolean,
    "cycleMethod": (string | string | string),
    "stops": [...{
    	"offset": number,
    	"color": Color
	}]
};
