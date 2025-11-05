import React, { useMemo, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import { useAuth } from '../../contexts/AuthContext';
import FloatingAppointmentCTA from '../../components/pharmacy/FloatingAppointmentCTA';

/**
 * 🛒 Catálogo (Autenticado) - Farmacia MediCitas
 * Igual a la vista de invitado pero con funciones desbloqueadas:
 * - Agregar al carrito
 * - Marcar como favorito
 * - Comprar ahora (simulado)
 *
 * Nota: Este componente reutiliza el contexto de autenticación (JWT) y
 * usa toasts para confirmar acciones. La lógica de carrito se maneja
 * localmente como demostración, lista para conectarse a un servicio real.
 */

const mockProducts = [
  { id: 1, name: 'Paracetamol 500mg', price: 8.5, description: 'Analgésico y antipirético' },
  { id: 2, name: 'Ibuprofeno 400mg', price: 12.9, description: 'Antiinflamatorio no esteroide' },
  { id: 3, name: 'Vitamina C 1000mg', price: 15.0, description: 'Refuerza el sistema inmunológico' },
  { id: 4, name: 'Alcohol en gel 250ml', price: 6.9, description: 'Higiene de manos' },
  { id: 5, name: 'Mascarillas (pack 10)', price: 9.9, description: 'Protección diaria' },
  { id: 6, name: 'Loratadina 10mg', price: 10.5, description: 'Antihistamínico' },
];

const promoSlides = [
  {
    id: 'promo-antigripales',
    title: 'Semana del Resfriado',
    subtitle: 'Hasta -20% en antigripales y vitamina C',
    ctaText: 'Ver selección',
    ctaLink: '/pharmacy/catalog',
    badge: 'Oferta',
  },
  {
    id: 'promo-cuidado-personal',
    title: 'Cuidado Personal',
    subtitle: 'Gel antibacterial y mascarillas con descuentos',
    ctaText: 'Explorar',
    ctaLink: '/pharmacy/catalog',
    badge: 'Especial',
  },
  {
    id: 'promo-alergias',
    title: 'Temporada de Alergias',
    subtitle: 'Aprovecha -15% en antihistamínicos',
    ctaText: 'Comprar ahora',
    ctaLink: '/pharmacy/catalog',
    badge: 'Top ventas',
  },
];

const PharmacyCatalogPage: React.FC = () => {
  const navigate = useNavigate();
  const { user, logout, isAuthenticated } = useAuth();

  // Estado local para carrito y favoritos (demostración)
  const [cart, setCart] = useState<{ id: number; qty: number }[]>([]);
  const [favorites, setFavorites] = useState<number[]>([]);

  const cartCount = useMemo(() => cart.reduce((sum, item) => sum + item.qty, 0), [cart]);

  const requireAuth = (actionLabel?: string) => {
    toast((t) => (
      <span>
        {actionLabel ? `${actionLabel} requiere iniciar sesión.` : 'Esta acción requiere iniciar sesión.'}
        <button
          onClick={() => {
            toast.dismiss(t.id);
            navigate('/pharmacy/login', { state: { returnTo: '/pharmacy/catalog' } });
          }}
          style={{ marginLeft: 8, color: 'var(--primary)' }}
        >
          Iniciar sesión
        </button>
      </span>
    ));
  };

  const addToCart = (productId: number) => {
    if (!isAuthenticated) {
      requireAuth('Agregar al carrito');
      return;
    }
    setCart((prev) => {
      const existing = prev.find((p) => p.id === productId);
      if (existing) {
        return prev.map((p) => (p.id === productId ? { ...p, qty: p.qty + 1 } : p));
      }
      return [...prev, { id: productId, qty: 1 }];
    });
    toast.success('Agregado al carrito');
  };

  const toggleFavorite = (productId: number) => {
    if (!isAuthenticated) {
      requireAuth('Favoritos');
      return;
    }
    setFavorites((prev) =>
      prev.includes(productId) ? prev.filter((id) => id !== productId) : [...prev, productId]
    );
    toast.success('Actualizado en favoritos');
  };

  const buyNow = (productId: number) => {
    if (!isAuthenticated) {
      requireAuth('Comprar ahora');
      return;
    }
    // Simulación de compra inmediata
    const product = mockProducts.find((p) => p.id === productId);
    toast.success(`Compra simulada de "${product?.name}" iniciada`);
  };

  const handleLogout = () => {
    try {
      // Cerrar sesión y limpiar estados locales del catálogo
      logout();
      setCart([]);
      setFavorites([]);
    } catch (error) {
      console.error('Error al cerrar sesión desde farmacia:', error);
    }
  };

  return (
    <div className="min-h-screen" style={{ fontFamily: 'Inter, sans-serif', backgroundColor: 'var(--background)' }}>
      {/* Header autenticado */}
      <header className="bg-white border-b px-4 sm:px-6 lg:px-8 py-4" style={{ borderColor: 'var(--border)' }}>
        <div className="max-w-7xl mx-auto flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="w-9 h-9 rounded-lg flex items-center justify-center" style={{ backgroundColor: 'var(--primary)' }}>
              <span className="text-white text-sm font-bold">FM</span>
            </div>
            <div>
              <h1 className="text-xl sm:text-2xl font-light" style={{ color: 'var(--text-primary)' }}>
                Catálogo — Farmacia MediCitas
              </h1>
              {isAuthenticated ? (
                <p className="text-xs" style={{ color: 'var(--text-secondary)' }}>
                  Sesión activa {user?.firstName ? `— Hola, ${user.firstName}` : ''}
                </p>
              ) : (
                <p className="text-xs" style={{ color: 'var(--text-secondary)' }}>
                  Estás explorando como invitado. Inicia sesión para comprar.
                </p>
              )}
            </div>
          </div>
          <nav className="flex items-center gap-2">
            <Link to="/pharmacy">
              <button className="btn-outline px-3 py-2 rounded-lg font-medium transition-colors">← Información</button>
            </Link>
            <div className="flex items-center gap-2 px-3 py-2 rounded-lg" style={{ border: '1px solid var(--border)', backgroundColor: 'var(--surface)' }}>
              <span className="material-icons" style={{ fontSize: 18, color: 'var(--text-secondary)' }}>shopping_cart</span>
              <span className="text-sm" style={{ color: 'var(--text-primary)' }}>Carrito</span>
              <span className="text-xs px-2 py-0.5 rounded-full" style={{ backgroundColor: 'var(--primary)', color: 'var(--text-on-primary)' }}>{cartCount}</span>
            </div>
            {/* Autenticación: mostrar login/register o logout */}
            {isAuthenticated ? (
              <button
                onClick={handleLogout}
                className="btn-outline px-3 py-2 rounded-lg font-medium transition-colors flex items-center gap-2"
              >
                <span className="material-icons" style={{ fontSize: 18 }}>logout</span>
                <span>Cerrar Sesión</span>
              </button>
            ) : (
              <div className="flex items-center gap-2">
                <button
                  className="btn-outline px-3 py-2 rounded-lg font-medium transition-colors"
                  onClick={() => navigate('/pharmacy/login', { state: { returnTo: '/pharmacy/catalog' } })}
                >
                  Iniciar Sesión
                </button>
                <button
                  className="btn-primary px-3 py-2 rounded-lg font-medium transition-colors"
                  onClick={() => navigate('/pharmacy/register', { state: { returnTo: '/pharmacy/catalog' } })}
                >
                  Crear Cuenta
                </button>
              </div>
            )}
          </nav>
        </div>
      </header>

      {/* Carrusel de promociones */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
        <section aria-label="Promociones y ofertas de la semana" className="mb-8">
          <div className="relative rounded-2xl overflow-hidden border shadow-sm group" style={{ borderColor: 'var(--border)', backgroundColor: 'var(--primary)' }}>
            {/* Solo mostramos el primer slide de forma estática en esta vista autenticada */}
            <div style={{ color: 'var(--text-on-primary)' }} className="px-6 sm:px-10 py-10 sm:py-12">
              <div className="flex items-center gap-2 mb-3">
                <span
                  className="text-xs px-2.5 py-1 rounded-full"
                  style={{
                    backgroundColor: 'color-mix(in srgb, var(--primary), black 15%)',
                    color: 'var(--text-on-primary-90)',
                    border: '1px solid color-mix(in srgb, var(--primary), black 35%)',
                  }}
                >
                  {promoSlides[0].badge}
                </span>
                <span className="text-xs" style={{ color: 'var(--text-on-primary-90)' }}>
                  Promociones exclusivas para cuentas activas
                </span>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 items-center">
                <div>
                  <h2 className="text-2xl sm:text-3xl font-semibold mb-2" style={{ color: 'var(--text-on-primary)' }}>
                    {promoSlides[0].title}
                  </h2>
                  <p className="text-sm sm:text-base mb-5" style={{ color: 'var(--text-on-primary-90)' }}>
                    {promoSlides[0].subtitle}
                  </p>
                  <div className="flex flex-wrap items-center gap-3">
                    <Link to={promoSlides[0].ctaLink}>
                      <button className="btn-white px-4 py-2 rounded-lg font-medium transition-colors" style={{ color: 'var(--primary)' }}>
                        {promoSlides[0].ctaText}
                      </button>
                    </Link>
                    <Link to="/pharmacy">
                      <button className="btn-outline-white px-4 py-2 rounded-lg font-medium transition-colors">
                        Más información
                      </button>
                    </Link>
                  </div>
                </div>

                <div className="h-40 sm:h-48 rounded-xl" style={{
                  backgroundImage: 'radial-gradient(ellipse at top left, color-mix(in srgb, var(--primary), white 20%) 0%, color-mix(in srgb, var(--primary), black 20%) 100%)',
                  opacity: 0.25,
                }} />
              </div>
            </div>
          </div>
        </section>

        {/* Listado de productos con acciones */}
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl sm:text-3xl font-light" style={{ color: 'var(--text-primary)' }}>
            Catálogo completo
          </h2>
          <Link to="/pharmacy">
            <button className="btn-outline px-4 py-2 rounded-lg font-medium transition-colors">← Volver</button>
          </Link>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {mockProducts.map((p) => {
            const isFav = favorites.includes(p.id);
            return (
              <div key={p.id} className="bg-white rounded-xl shadow-sm p-6 hover:shadow-md transition" style={{ border: '1px solid var(--border)' }}>
                <div className="w-full h-32 mb-4 rounded-lg flex items-center justify-end relative" style={{ backgroundColor: 'var(--primary)', opacity: 0.12 }}>
                  {!isAuthenticated && (
                    <div className="absolute left-3 top-3 flex items-center gap-1 text-xs px-2 py-1 rounded-md"
                         style={{ backgroundColor: 'var(--surface)', border: '1px solid var(--border)', color: 'var(--text-secondary)' }}>
                      <span className="material-icons" style={{ fontSize: 16 }}>lock</span>
                      <span>Inicia sesión para usar funciones</span>
                    </div>
                  )}
                  <button
                    aria-label={isFav ? 'Quitar de favoritos' : 'Agregar a favoritos'}
                    onClick={() => (isAuthenticated ? toggleFavorite(p.id) : requireAuth('Favoritos'))}
                    className="w-9 h-9 rounded-full flex items-center justify-center mr-2"
                    style={{
                      backgroundColor: isFav ? 'color-mix(in srgb, var(--primary), black 10%)' : 'var(--surface)',
                      border: '1px solid var(--border)'
                    }}
                  >
                    <span className="material-icons" style={{ color: isFav ? 'var(--primary)' : 'var(--text-secondary)' }}>
                      {isFav ? 'favorite' : 'favorite_border'}
                    </span>
                  </button>
                </div>
                <h3 className="text-lg font-medium mb-1" style={{ color: 'var(--text-primary)' }}>{p.name}</h3>
                <p className="text-sm mb-3" style={{ color: 'var(--text-secondary)' }}>{p.description}</p>
                <div className="flex items-center justify-between mb-3">
                  <span className="text-base font-semibold" style={{ color: 'var(--text-primary)' }}>
                    ${p.price.toFixed(2)}
                  </span>
                  <div className="flex items-center gap-2">
                    <button
                      className="btn-outline px-3 py-2 rounded-md text-sm"
                      onClick={() => (isAuthenticated ? addToCart(p.id) : requireAuth('Agregar al carrito'))}
                      aria-disabled={!isAuthenticated}
                      style={{ opacity: !isAuthenticated ? 0.6 : 1, cursor: !isAuthenticated ? 'not-allowed' : 'pointer' }}
                    >
                      Agregar
                    </button>
                    <button
                      className="btn-primary px-3 py-2 rounded-md text-sm"
                      onClick={() => (isAuthenticated ? buyNow(p.id) : requireAuth('Comprar ahora'))}
                      aria-disabled={!isAuthenticated}
                      style={{ opacity: !isAuthenticated ? 0.6 : 1, cursor: !isAuthenticated ? 'not-allowed' : 'pointer' }}
                    >
                      Comprar ahora
                    </button>
                  </div>
                </div>
                <div className="flex items-center justify-between">
                  <Link to={`/pharmacy/product/${p.id}`}>
                    <button className="btn-secondary px-3 py-2 rounded-md text-sm">Ver detalles</button>
                  </Link>
                  <span className="text-xs" style={{ color: 'var(--text-secondary)' }}>
                    {isAuthenticated ? (isFav ? 'En favoritos' : 'Añade a favoritos para guardar') : 'Inicia sesión para guardar en favoritos'}
                  </span>
                </div>
              </div>
            );
          })}
        </div>

        <div className="mt-10 text-center">
          {isAuthenticated ? (
            <p className="text-sm" style={{ color: 'var(--text-secondary)' }}>
              ¡Disfruta de todas las funciones! Tu sesión está activa.
            </p>
          ) : (
            <div className="inline-flex items-center gap-3 px-4 py-3 rounded-lg"
                 style={{ border: '1px solid var(--border)', backgroundColor: 'var(--surface)' }}>
              <span className="material-icons" style={{ fontSize: 18, color: 'var(--text-secondary)' }}>info</span>
              <p className="text-sm" style={{ color: 'var(--text-secondary)' }}>
                Para comprar o guardar favoritos, inicia sesión o crea tu cuenta.
              </p>
              <button
                className="btn-primary px-3 py-2 rounded-md text-sm"
                onClick={() => navigate('/pharmacy/login', { state: { returnTo: '/pharmacy/catalog' } })}
              >
                Iniciar Sesión
              </button>
            </div>
          )}
        </div>
      </main>

      {/* Footer */}
      <footer className="bg-white py-8" style={{ borderTop: '1px solid var(--border)' }}>
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <p className="text-sm" style={{ color: 'var(--text-secondary)' }}>
            Vista autenticada — Funciones habilitadas.
          </p>
        </div>
      </footer>

      {/* Botón flotante para agendar nueva cita (solo clientes) */}
      {isAuthenticated && <FloatingAppointmentCTA />}
    </div>
  );
};

export default PharmacyCatalogPage;