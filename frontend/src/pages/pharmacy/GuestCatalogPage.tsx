import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

/**
 * 🛒 Catálogo (Invitado) - Farmacia MediCitas
 * Muestra un listado básico de productos accesible sin autenticación.
 */
const mockProducts = [
  { id: 1, name: 'Paracetamol 500mg', price: 8.5, description: 'Analgésico y antipirético' },
  { id: 2, name: 'Ibuprofeno 400mg', price: 12.9, description: 'Antiinflamatorio no esteroide' },
  { id: 3, name: 'Vitamina C 1000mg', price: 15.0, description: 'Refuerza el sistema inmunológico' },
  { id: 4, name: 'Alcohol en gel 250ml', price: 6.9, description: 'Higiene de manos' },
  { id: 5, name: 'Mascarillas (pack 10)', price: 9.9, description: 'Protección diaria' },
  { id: 6, name: 'Loratadina 10mg', price: 10.5, description: 'Antihistamínico' },
];

// Slides de promoción/ofertas de la semana
const promoSlides = [
  {
    id: 'promo-antigripales',
    title: 'Semana del Resfriado',
    subtitle: 'Hasta -20% en antigripales y vitamina C',
    ctaText: 'Ver selección',
    ctaLink: '/pharmacy/catalog?promo=resfriado',
    badge: 'Oferta',
  },
  {
    id: 'promo-cuidado-personal',
    title: 'Cuidado Personal',
    subtitle: 'Gel antibacterial y mascarillas con descuentos',
    ctaText: 'Explorar',
    ctaLink: '/pharmacy/catalog?promo=cuidado',
    badge: 'Especial',
  },
  {
    id: 'promo-alergias',
    title: 'Temporada de Alergias',
    subtitle: 'Aprovecha -15% en antihistamínicos',
    ctaText: 'Comprar ahora',
    ctaLink: '/pharmacy/catalog?promo=alergias',
    badge: 'Top ventas',
  },
];

const GuestCatalogPage: React.FC = () => {
  const [current, setCurrent] = useState(0);
  const [paused, setPaused] = useState(false);

  useEffect(() => {
    if (paused) return;
    const id = setInterval(() => {
      setCurrent((prev) => (prev + 1) % promoSlides.length);
    }, 5000);
    return () => clearInterval(id);
  }, [paused]);

  const goPrev = () => setCurrent((prev) => (prev - 1 + promoSlides.length) % promoSlides.length);
  const goNext = () => setCurrent((prev) => (prev + 1) % promoSlides.length);

  return (
    <div className="min-h-screen" style={{ fontFamily: 'Inter, sans-serif', backgroundColor: 'var(--background)' }}>
      {/* Header con acceso */}
      <header className="bg-white border-b px-4 sm:px-6 lg:px-8 py-4" style={{ borderColor: 'var(--border)' }}>
        <div className="max-w-7xl mx-auto flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="w-9 h-9 rounded-lg flex items-center justify-center" style={{ backgroundColor: 'var(--primary)' }}>
              <span className="text-white text-sm font-bold">FM</span>
            </div>
            <h1 className="text-xl sm:text-2xl font-light" style={{ color: 'var(--text-primary)' }}>
              Catálogo — Farmacia MediCitas
            </h1>
          </div>
          <nav className="flex items-center space-x-3">
            <Link to="/pharmacy/login">
              <button className="btn-outline px-4 py-2 rounded-lg font-medium transition-colors">Iniciar Sesión</button>
            </Link>
            <Link to="/pharmacy/register">
              <button className="btn-primary px-4 py-2 rounded-lg font-medium transition-colors">Crear Cuenta</button>
            </Link>
          </nav>
        </div>
      </header>

      {/* Carrusel de promociones/ofertas de la semana */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
        <section aria-label="Promociones y ofertas de la semana" className="mb-8">
          <div
            className="relative rounded-2xl overflow-hidden border shadow-sm group"
            style={{ borderColor: 'var(--border)', backgroundColor: 'var(--primary)' }}
            onMouseEnter={() => setPaused(true)}
            onMouseLeave={() => setPaused(false)}
          >
            {/* Slide actual */}
            {promoSlides.map((slide, idx) => (
              <div
                key={slide.id}
                className={`transition-opacity duration-500 ${idx === current ? 'opacity-100' : 'opacity-0 absolute inset-0 pointer-events-none'}`}
                style={{ color: 'var(--text-on-primary)' }}
              >
                <div className="px-6 sm:px-10 py-10 sm:py-12">
                  <div className="flex items-center gap-2 mb-3">
                    <span
                      className="text-xs px-2.5 py-1 rounded-full"
                      style={{
                        backgroundColor: 'color-mix(in srgb, var(--primary), black 15%)',
                        color: 'var(--text-on-primary-90)',
                        border: '1px solid color-mix(in srgb, var(--primary), black 35%)',
                      }}
                    >
                      {slide.badge}
                    </span>
                    <span className="text-xs" style={{ color: 'var(--text-on-primary-90)' }}>
                      Promociones actualizadas semanalmente
                    </span>
                  </div>

                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-6 items-center">
                    <div>
                      <h2 className="text-2xl sm:text-3xl font-semibold mb-2" style={{ color: 'var(--text-on-primary)' }}>
                        {slide.title}
                      </h2>
                      {slide.subtitle && (
                        <p className="text-sm sm:text-base mb-5" style={{ color: 'var(--text-on-primary-90)' }}>
                          {slide.subtitle}
                        </p>
                      )}
                      <div className="flex flex-wrap items-center gap-3">
                        <Link to={slide.ctaLink || '/pharmacy/catalog'}>
                          <button
                            className="btn-white px-4 py-2 rounded-lg font-medium transition-colors"
                            style={{ color: 'var(--primary)' }}
                          >
                            {slide.ctaText || 'Ver catálogo'}
                          </button>
                        </Link>
                        <Link to="/pharmacy">
                          <button className="btn-outline-white px-4 py-2 rounded-lg font-medium transition-colors">
                            Más información
                          </button>
                        </Link>
                      </div>
                    </div>

                    {/* Visual decorativo */}
                    <div className="h-40 sm:h-48 rounded-xl" style={{
                      backgroundImage: 'radial-gradient(ellipse at top left, color-mix(in srgb, var(--primary), white 20%) 0%, color-mix(in srgb, var(--primary), black 20%) 100%)',
                      opacity: 0.25,
                    }} />
                  </div>
                </div>
              </div>
            ))}

            {/* Controles */}
            <button
              aria-label="Anterior"
              onClick={goPrev}
              className="absolute left-3 top-1/2 -translate-y-1/2 w-10 h-10 rounded-full flex items-center justify-center"
              style={{
                backgroundColor: 'color-mix(in srgb, var(--primary), black 25%)',
                border: '1px solid color-mix(in srgb, var(--primary), black 40%)',
                color: 'var(--text-on-primary)',
              }}
            >
              <span className="material-icons">chevron_left</span>
            </button>
            <button
              aria-label="Siguiente"
              onClick={goNext}
              className="absolute right-3 top-1/2 -translate-y-1/2 w-10 h-10 rounded-full flex items-center justify-center"
              style={{
                backgroundColor: 'color-mix(in srgb, var(--primary), black 25%)',
                border: '1px solid color-mix(in srgb, var(--primary), black 40%)',
                color: 'var(--text-on-primary)',
              }}
            >
              <span className="material-icons">chevron_right</span>
            </button>

            {/* Indicadores */}
            <div className="absolute bottom-3 left-0 right-0 flex items-center justify-center gap-2">
              {promoSlides.map((_, idx) => (
                <button
                  key={idx}
                  aria-label={`Ir al slide ${idx + 1}`}
                  onClick={() => setCurrent(idx)}
                  className="w-2.5 h-2.5 rounded-full"
                  style={{
                    backgroundColor: idx === current ? 'var(--text-on-primary)' : 'color-mix(in srgb, var(--primary), black 45%)',
                    border: '1px solid color-mix(in srgb, var(--primary), black 55%)',
                  }}
                />
              ))}
            </div>
          </div>
        </section>

        {/* Listado de productos */}
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl sm:text-3xl font-light" style={{ color: 'var(--text-primary)' }}>
            Explora como invitado
          </h2>
          <Link to="/pharmacy">
            <button className="btn-outline px-4 py-2 rounded-lg font-medium transition-colors">← Volver</button>
          </Link>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {mockProducts.map((p) => (
            <div key={p.id} className="bg-white rounded-xl shadow-sm p-6 hover:shadow-md transition" style={{ border: '1px solid var(--border)' }}>
              <div className="w-full h-32 mb-4 rounded-lg" style={{ backgroundColor: 'var(--primary)', opacity: 0.12 }} />
              <h3 className="text-lg font-medium mb-1" style={{ color: 'var(--text-primary)' }}>{p.name}</h3>
              <p className="text-sm mb-3" style={{ color: 'var(--text-secondary)' }}>{p.description}</p>
              <div className="flex items-center justify-between">
                <span className="text-base font-semibold" style={{ color: 'var(--text-primary)' }}>
                  ${p.price.toFixed(2)}
                </span>
                <button className="btn-primary px-3 py-2 rounded-md text-sm">Ver detalles</button>
              </div>
            </div>
          ))}
        </div>

        <div className="mt-10 text-center">
          <p className="text-sm" style={{ color: 'var(--text-secondary)' }}>
            Para comprar necesitarás iniciar sesión o crear tu cuenta.
          </p>
        </div>
      </main>

      {/* Footer */}
      <footer className="bg-white py-8" style={{ borderTop: '1px solid var(--border)' }}>
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <p className="text-sm" style={{ color: 'var(--text-secondary)' }}>
            Vista de invitado — Algunas funciones pueden requerir autenticación.
          </p>
        </div>
      </footer>
    </div>
  );
};

export default GuestCatalogPage;