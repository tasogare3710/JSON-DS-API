use standard;

// hoisted version
type JSON = Layer;

type Layer = {
	"name":			string,
	"frame":		string,
	"x":			number,
	"y":			number,
	"replaceKey":   Color,
	"replaceColor": ReplaceColorType
};

type ReplaceColorType = (ColorOfReplaceColorKind | LinearGradientOfReplaceColorKind | RadialGradientOfReplaceColorKind);

type RadialGradientOfReplaceColorKind = {
	"RadialGradient": RadialGradient
}

type LinearGradientOfReplaceColorKind = {
	"LinearGradient": LinearGradient
}

type ColorOfReplaceColorKind = {
	"Color": Color
}

type RadialGradient = {
    "focusAngle":	 number,
    "focusDistance": number,
    "centerX":		 number,
    "centerY":       number,
    "radius":        number,
    "proportional":  boolean,
    "cycleMethod":   CycleMethod,
    "stops":         Stops
};

type LinearGradient = {
    "startX":       number,
    "startY":       number,
    "endX":         number,
    "endY":         number,
    "proportional": boolean,
    "cycleMethod":  CycleMethod,
    "stops":        Stops
};

type CycleMethod = (NO_CYCLE | REFLECT | REPEAT);

type NO_CYCLE = string;
type REFLECT = string;
type REPEAT = string;

type Stops = [...Stop];

type Stop = {
    "offset": number,
    "color":  Color
};

type Color = {
    "red":     number,
    "green":   number,
    "blue":    number,
    "opacity": number
};
