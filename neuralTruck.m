t = tcpip('localhost', 4321);
fopen(t);
fprintf(t,'r');
data_t = fscanf(t,'%f %f %f');
fprintf('oi');
while(~isempty(data_t))
    virar = sim(net,data_t([1 3])); % 1 -> x, 2 -> y , 3 -> angulo; 
    fprintf(t, '%f\n', virar);
    fprintf(t,'r');
    data_t = fscanf(t,'%f %f %f');
end
