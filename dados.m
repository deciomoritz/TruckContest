%%import data
clear all;
close all;
x = importdata('x.txt')
x = x.textdata
x = str2double(x)

y = importdata('y.txt')
y = y.textdata
y = str2double(y)

angle = importdata('angle.txt')
angle = angle.textdata
angle = str2double(angle)

turn = importdata('turn.txt')
turn = turn.textdata
turn = str2double(turn)

hold on;
grid on
plot3(x,angle,turn,'*');

dataset = [x; y; angle; turn;]

%%fucking ugly data, holy shit

%%
%%train the gooddamned net!

nftool
%%