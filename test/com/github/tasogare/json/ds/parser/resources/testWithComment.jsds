//TODO datatypeが未定義

type Stop = {
    "offset": number,
    "color": Color
};

type Stops = [...Stop];

type Color = {
    "red": number,
    "green": number,
    "blue": number,
    "opacity": number
};

//TODO これでどうか？
type NO_CYCLE = string;
type REFLECT = string;
type REPEAT = string;
type CycleMethod = (NO_CYCLE | REFLECT | REPEAT);
//TODO 表現できない。UnionTypeを拡張するか？
//type CycleMethod = ("NO_CYCLE" | "REFLECT" | "REPEAT");

type LinearGradient = {
    "startX": number,
    "startY": number,
    "endX": number,
    "endY": number,
    "proportional": boolean,
    "cycleMethod": CycleMethod,
    "stops": Stops
};

type ColorOfReplaceColorKind = {
	"Color": Color
}

type LinearGradientOfReplaceColorKind = {
	"LinearGradient": LinearGradient
}

type RadialGradientOfReplaceColorKind = {
	"RadialGradient": RadialGradient
}

type RadialGradient = {
    "focusAngle": number,
    "focusDistance": number,
    "centerX": number,
    "centerY": number,
    "radius": number,
    "proportional": boolean,
    "cycleMethod": CycleMethod,
    "stops": Stops
};

type ReplaceColorType = (ColorOfReplaceColorKind | LinearGradientOfReplaceColorKind | RadialGradientOfReplaceColorKind);

type Layer = {
	"name": string,
	"frame": string,
	"x": int,
	"y": int,
	"replaceKey": Color,
	"replaceColor": ReplaceColorType
};

type JSON = Layer;
