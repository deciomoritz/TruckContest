%%import data
clear all;
close all;
x = importdata('x.txt');
x = x.textdata;
x = str2double(x);

y = importdata('y.txt');
y = y.textdata;
y = str2double(y);

angle = importdata('angle.txt');
angle = angle.textdata;
angle = str2double(angle);

turn = importdata('turn.txt');
turn = turn.textdata;
turn = str2double(turn);

hold on;
grid on;
plot3(x,angle,turn,'*');

dataset = [x; y; angle;];

%%fucking ugly data, holy shit

%%
%%train the gooddamned net!

net = newff(dataset,turn,10,{'tansig', 'purelin'},'trainlm');
net.trainParam.epochs=10000; % máximo de épocas
net.trainParam.goal=1.e-7;                 % erro mínimo 
net=train(net,dataset,turn);

t = tcpip('localhost', 4321);
fopen(t);
fprintf(t,'r');
data_t = fscanf(t,'%f %f %f');
while(~isempty(data_t))
    data = [data_t(1) data_t(2) data_t(3)]';
    virar = sim(net,data);
    fprintf(t, '%f\n', virar);
    fprintf(t,'r');
    data_t = fscanf(t,'%f %f %f');
end

%%