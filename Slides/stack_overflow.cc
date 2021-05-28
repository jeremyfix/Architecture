

int f(int n) {
  int x = f(n-1);
  return 2*x+1;
}

int main(int argc, char* argv[]){
  f(4);
}
